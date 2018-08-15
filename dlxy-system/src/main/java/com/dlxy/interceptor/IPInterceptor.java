package com.dlxy.interceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.dlxy.common.dto.VisitUserHistoryDTO;
import com.dlxy.common.dto.VisitUserHistoryDTO.HistroyDetail;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.service.IRedisService;
import com.google.gson.reflect.TypeToken;
import com.joker.library.utils.CommonUtils;
import com.joker.library.utils.DateUtils;

public class IPInterceptor implements HandlerInterceptor
{
	private Long visitIntervalTime = 1000 * 30L;

	private Integer maxRefreshTimes = 5;

	@Autowired
	private IRedisService redisService;

	private Logger logger = LoggerFactory.getLogger(IPInterceptor.class);

	private class JudgeResult
	{
		private boolean filter;
		private Long remainSeconds;

		public boolean isFilter()
		{
			return filter;
		}

		public void setFilter(boolean filter)
		{
			this.filter = filter;
		}

		public Long getRemainSeconds()
		{
			return remainSeconds;
		}

		public void setRemainSeconds(Long remainSeconds)
		{
			this.remainSeconds = remainSeconds;
		}

	}

	private JudgeResult judgeFromHistory(HttpServletRequest request, String ip, String uri)
	{
		HttpSession session = request.getSession();
		if (session == null)
		{
			return null;
		}
		VisitUserHistoryDTO historyDTO = null;
		historyDTO = (VisitUserHistoryDTO) session.getAttribute("history");
		JudgeResult result = this.new JudgeResult();

		if (historyDTO == null)
		{
			String json = redisService.get(String.format(IRedisService.USER_VISIT_HISTORY, ip));
			if (StringUtils.isEmpty(json))
			{
				result.setFilter(true);
				historyDTO = new VisitUserHistoryDTO();
				historyDTO.setIp(ip);
				session.setAttribute("history", historyDTO);
				return result;
			}
			historyDTO = JsonUtil.json2Object(json, VisitUserHistoryDTO.class);
		}
		HistroyDetail detail = historyDTO.getDetails().get(uri);
		if(null==detail)
		{
			result.setFilter(true);
			detail=new HistroyDetail();
			historyDTO.getDetails().put(uri, detail);
			return result;
		}
		Integer refreshCounts = detail.getRefreshCounts();
		Integer maxVisitCount = detail.getMaxLimitedCounts();
		if (refreshCounts < maxVisitCount)
		{
			result.setFilter(true);
		} else
		{
			Long lastVisitTimes = historyDTO.getLastVisitTimes(uri);
			Long remainSeconds = System.currentTimeMillis() - lastVisitTimes;
			// 关于时间判断可以根据数学公式做动态深入的设计
			if (remainSeconds < visitIntervalTime)
			{
				result.setFilter(false);
				result.setRemainSeconds(visitIntervalTime - remainSeconds);
			} else
			{
				// 关于次数限制可以根据数学公式做动态深入的设计
				detail.setMaxLimitedCounts(maxVisitCount + maxRefreshTimes);
				result.setFilter(true);
			}
		}
		return result;
	}

	private void rediretct(HttpServletResponse response, String error) throws IOException
	{
		error = URLEncoder.encode(error, "utf-8");
		response.sendRedirect("/public/banned.html?error=" + error);
		response.setStatus(HttpStatus.FORBIDDEN.ordinal());
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{

		boolean needIpFilter = true;
		request.getSession();
		// 更新全站的访问次数和,日访问次数,不限ip,一旦访问就增加
		// try
		// {
		// redisService.incr(IRedisService.WEB_VISIT_TOTAL_COUT);
		// String perKey=String.format(IRedisService.PER_DAY_VISIT_COUNT,
		// DateUtils.getCurrentDay());
		// String perDayJson = redisService.get(perKey);
		// if(StringUtils.isEmpty(perDayJson))
		// {
		// redisService.set(perKey, "1",60*60*48);
		// }else {
		// redisService.incr(perKey);
		// }
		// } catch (Exception e)
		// {
		// logger.error("[更新全站访问次数],可能是redis服务器挂了:{}",e.getMessage());
		// return true;
		// }

		if (!needIpFilter)
		{
			return true;
		}
		String uri = request.getRequestURI();
		String ip = CommonUtils.getIpAddr(request);
		try
		{
			String ipJsonStr = redisService.get(IRedisService.BANED_IP);
			if (!org.apache.commons.lang3.StringUtils.isEmpty(ipJsonStr))
			{
				List<String> ips = JsonUtil.json2List(ipJsonStr, new TypeToken<String>()
				{
				}.getType());
				if (ips.contains(ip))
				{
					String error = URLEncoder.encode("对不起,你已经被禁止访问,请明日再来访问", "utf-8");
					rediretct(response, error);
					return false;
				}
			}
			JudgeResult result = judgeFromHistory(request, ip, uri);
			if (null == result)
			{
				return true;
			}
			if (!result.isFilter())
			{
				rediretct(response, "刷新频率太快了,请过" + result.getRemainSeconds() / 1000 + "秒后再访问");
				return false;
			}
			// 通过ip显示每日访问人数,如果不想通过ip,每来一次访问都增加,则注释这段然后直接打开上面注释的那段即可
			// 如果想跟文章采取的策略相同,则注释这段 打开下面这段即可,全站的访问策略采取的是跟文章相同
			// 注意更改策略同时记得更改数据结构,同时尽量做到redis高可用,不然会出现数据丢失状况
			// String perKey=String.format(IRedisService.PER_DAY_VISIT_COUNT,
			// DateUtils.getCurrentDay());
			// String perDayJson = redisService.get(perKey);
			// Map<String, Object> map=null;
			// Set<String>ips=null;
			// if(!StringUtils.isEmpty(perDayJson))
			// {
			// map= JsonUtil.json2Map(perDayJson, Map.class);
			// ips=(Set<String>) map.get("ips");
			// if(!ips.contains(ip))
			// {
			// Long visitCount = (Long) map.get("visitCount");
			// visitCount++;
			// }
			// }else {
			// map=new HashMap<String, Object>();
			// map.put("visitCount", 1);
			// ips=new HashSet<>();
			// ips.add(ip);
			// redisService.expire(perKey, 60*60*48);
			// }
			// String newJson = JsonUtil.obj2Json(map);
			// redisService.set(perKey, newJson);
			else
			{
				//有刷新次数限制
				String perKey = String.format(IRedisService.PER_DAY_VISIT_COUNT, DateUtils.getCurrentDay());
				String perDayJson = redisService.get(perKey);
				redisService.incr(IRedisService.WEB_VISIT_TOTAL_COUT);
				if (StringUtils.isEmpty(perDayJson))
				{
					redisService.set(perKey, "1", 60 * 60 * 48);
				} else
				{
					redisService.incr(perKey);
				}
			}

		} catch (Exception e)
		{
			logger.error("[IPfilter]error:{}", e.getMessage());
			return true;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
		boolean needIpFilter = true;
		if (!needIpFilter)
		{
			return;
		}
		HttpSession session = request.getSession();
		if (null == session)
		{
			return;
		}
		VisitUserHistoryDTO visitUserHistoryDTO = (VisitUserHistoryDTO) session.getAttribute("history");
		try
		{
			if (visitUserHistoryDTO == null)
			{
				return;
			}
			Map<String, HistroyDetail> mapDetail = visitUserHistoryDTO.getDetails();
			HistroyDetail detail = mapDetail.get(request.getRequestURI());
//			if (null == detail)
//			{
//				detail = new HistroyDetail();
//			}
			detail.setLastVisitTime(System.currentTimeMillis());
			detail.incr();
//			mapDetail.put(request.getRequestURI(), detail);
			String json = JsonUtil.obj2Json(visitUserHistoryDTO);
			String key = String.format(IRedisService.USER_VISIT_HISTORY, visitUserHistoryDTO.getIp());
			redisService.set(key, json);
			redisService.expire(key, 60 * 60 * 2);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[IPfilter]redis服务器挂了");
			return;
		}
	}
}
