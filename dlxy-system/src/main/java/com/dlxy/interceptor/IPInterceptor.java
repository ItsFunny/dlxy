/**
*
* @author joker 
* @date 创建时间：2018年8月1日 下午12:40:45
* 
*/
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

public class IPInterceptor implements HandlerInterceptor
{
	private Long visitIntervalTime = 1000 * 30L;

	private Integer maxRefreshTimes = 5;

	

//	private ThreadLocal<VisitUserHistoryDTO> histroy = new ThreadLocal<>();

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

	private JudgeResult judgeFromHistory(HttpServletRequest request,String ip, String uri)
	{
//		IPInterceptor interceptor=new IPInterceptor();
		
		
		HttpSession session = request.getSession(true);
		VisitUserHistoryDTO historyDTO =null;
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
		Integer refreshCounts = detail.getRefreshCounts();
		Integer maxVisitCount=detail.getMaxLimitedCounts();
		if (refreshCounts < maxVisitCount)
		{
			result.setFilter(true);
		} else
		{
			Long lastVisitTimes = historyDTO.getLastVisitTimes(uri);
			Long remainSeconds = System.currentTimeMillis() - lastVisitTimes;
			//关于时间判断可以根据数学公式做动态深入的设计
			if (remainSeconds < visitIntervalTime)
			{
				result.setFilter(false);
				result.setRemainSeconds(visitIntervalTime - remainSeconds);
			}else {
				//关于次数限制可以根据数学公式做动态深入的设计
				detail.setMaxLimitedCounts(maxVisitCount+maxRefreshTimes);
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
		boolean needIpFilter=true;
		if (!needIpFilter)
		{
			return true;
		}
		String uri = request.getRequestURI();
		String ip = CommonUtils.getIpAddr(request);
		try
		{
			String ipJsonStr = redisService.get(IRedisService.BANED_IP);
			if(org.apache.commons.lang3.StringUtils.isEmpty(ipJsonStr))
			{
				List<String> ips = JsonUtil.json2List(ipJsonStr, new TypeToken<String>()
				{
				}.getType());
				if (ips.contains(ip))
				{
					String error = URLEncoder.encode("对不起,你已经被禁止访问,请明日再来访问", "utf-8");
					rediretct(response, error);
				}
			}
			JudgeResult result = judgeFromHistory(request,ip, uri);
			if (!result.isFilter())
			{
				rediretct(response, "刷新频率太快了,请过" + result.getRemainSeconds() / 1000 + "秒后再访问");
				return false;
			}
		} catch (Exception e)
		{
			logger.error("[IPfilter]error:{}",e.getMessage());
			return true;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
		boolean needIpFilter=true;
		if (!needIpFilter)
		{
			return;
		}
		VisitUserHistoryDTO  visitUserHistoryDTO=(VisitUserHistoryDTO) request.getSession(true).getAttribute("history");
		try
		{
//			VisitUserHistoryDTO visitUserHistoryDTO = histroy.get();
//			if (visitUserHistoryDTO == null)
//			{
//				visitUserHistoryDTO = new VisitUserHistoryDTO();
//				// detail = new HistroyDetail();
//			}
			// else
			// {
			// detail = visitUserHistoryDTO.getDetails().get(request.getRequestURI());
			// if (null == detail)
			// {
			// detail = new HistroyDetail();
			// }
			// }
			Map<String, HistroyDetail> mapDetail = visitUserHistoryDTO.getDetails();
			HistroyDetail detail = mapDetail.get(request.getRequestURI());
			if (null == detail)
			{
				detail = new HistroyDetail();
			}
			detail.setLastVisitTime(System.currentTimeMillis());
			detail.incr();
			mapDetail.put(request.getRequestURI(), detail);
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
