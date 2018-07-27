/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午9:27:47
* 
*/
package com.dlxy.service.impl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.model.ArticleVisitCountFactory;
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleCountService;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.service.AbstractRecordDetailHandler;
import com.dlxy.service.ArticleVisitCountContext;
import com.dlxy.service.DbVisitCountStrategy;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IRedisService;
import com.dlxy.service.RedisArticleVIsitCountStrategy;
import com.dlxy.utils.FileUtil;
import com.dlxy.vo.TitleDetailVO;
import com.joker.library.utils.CommonUtils;


/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 上午9:27:47
 */
public class ManagementArticleServiceObservableImpl extends Observable implements IArticleManagementWrappedService
{

	@Autowired
	private IArticleService articleService;
	@Autowired
	private IArticleCountService articleCountService;

	@Autowired
	private ITitleService titleService;

	@Autowired
	private AppEventPublisher appEventPublisher;
	@Autowired
	private IPictureService pictureService;

	@Autowired
	private IRedisService redisService;
	@Autowired
	private ArticleVisitCountContext articleVisitCountContext;
	
//	{
//		//这样似乎不太好,但是又不想放到ioc中  ,还是放到ioc中吧,还是改为静态类了,因为将flyweight和strategy整到一起去了,
		//放到ioc中得话context也需要注册
//		ArticleVisitCountFactory.init(redisService, articleService);
//	}

	@Override
	public Integer findArticleVisitCount(Long articleId, String ip)
	{
		int result = 0;
//		ArticleVisitInfo visitInfo = null;
//		ArticleVisitCountContext context = new ArticleVisitCountContext(articleService,redisService);
//		visitInfo = articleVisitCountFactory.get(articleId);
//		if(redisService.isAvaliable())
//		{
//			context = new ArticleVisitCountContext(new RedisArticleVIsitCountStrategy(redisService, articleService));
//		}else {
//			context=new ArticleVisitCountContext(new DbVisitCountStrategy(articleService));
//		}
		result = articleVisitCountContext.visitCount(articleId);
		// String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, articleId);
		// String json = redisService.get(key);
		// ArticleVisitInfo visitInfo = null;
		// if (StringUtils.isEmpty(json))
		// {
		// try
		// {
		// ArticleDTO articleDTO = articleService.findByArticleId(articleId);
		//// visitInfo = ArticleVisitCountFactory.create();
		// if (null == articleDTO)
		// {
		// visitInfo=ArticleVisitCountFactory.create(0);
		// throw new RuntimeException("文章不存在");
		// } else
		// {
		// visitInfo=ArticleVisitCountFactory.create(articleDTO.getVisitCount());
		// }
		// // redisService.set(key, JsonUtil.obj2Json(visitInfo));
		// }catch (Exception e) {
		// e.printStackTrace();
		// throw new JedisException("redis服务器挂了");
		// }
		// } else
		// {
		// visitInfo = JsonUtil.json2Object(json, ArticleVisitInfo.class);
		// }
		// synchronized (visitInfo)
		// {
		// Map<String, Long> visitors = visitInfo.getVisitors();
		// boolean needIncr = true;
		// if (null != visitors)
		// {
		// Long lastVisitTime = visitors.get(ip);
		// if (null != lastVisitTime && (System.currentTimeMillis() - lastVisitTime) <
		// 1000*90 )
		// {
		// needIncr = false;
		// }
		//
		// } else
		// {
		// visitors = new HashMap<>();
		// }
		// if (needIncr)
		// {
		// visitors.put(ip, System.currentTimeMillis());
		// visitInfo.incr();
		// }
		// result=visitInfo.get();
		// }
		// try
		// {
		// redisService.set(key, JsonUtil.obj2Json(visitInfo));
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// throw new JedisException("redis服务器挂了");
		// }
		return result;
	}

	@Override
	public PageDTO<Collection<ArticleDTO>> findByParams(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		Long totalCount = articleCountService.countArticlesByDetailParam(params);
		if (totalCount <= 0)
		{
			return PageResultUtil.emptyPage();
		}
		if (pageSize < 1)
		{
			pageSize = 1;
		}
		if (pageNum < 1)
		{
			pageNum = 1;
		}
		if (params.containsKey("q"))
		{
			params.put("searchParam", params.get("q"));
		}
		Collection<ArticleDTO> datas = articleService.findByParam(params, (pageNum - 1) * pageSize, pageSize);
		return new PageDTO<Collection<ArticleDTO>>(totalCount, datas);
	}

	@Transactional
	@Override
	public void updateArticlesInBatch(UserDTO userDTO, Long[] articleIds, int status)
	{
		articleService.updateArticleStatusInBatch(articleIds, status);
		StringBuilder sBuilder = new StringBuilder();
		for (Long long1 : articleIds)
		{
			sBuilder.append(long1 + ",");
		}
		execute(userDTO, "修改文章状态:" + AbstractRecordDetailHandler.ARTICLE + ":" + sBuilder + ": 状态->" + status);
	}

	@Transactional
	@Override
	public void updateArticleTypeInBatch(UserDTO userDTO, Long[] articleIds, int type)
	{
		articleService.updateArticleTypeInbatch(articleIds, type);
		StringBuilder sBuilder = new StringBuilder();
		for (Long long1 : articleIds)
		{
			sBuilder.append(long1 + ",");
		}
		execute(userDTO, "修改文章类型:" + AbstractRecordDetailHandler.ARTICLE + ":" + sBuilder + ":类型-> " + type);
	}

	@Override
	public ArticleDTO findArticleDetailByArticleId(Long articleId) throws SQLException
	{

		return articleService.findArticleDetailByArticleId(articleId);
	}

	@Override
	public ArticleDTO showArticleDetail(Long articleId) throws SQLException
	{
		ArticleDTO articleDTO = articleService.findArticlePrevAndNext(articleId);
		if (null == articleDTO)
		{
			return null;
		}
		DlxyTitleDTO belongTo = titleService.findById(articleDTO.getTitleId());
		articleDTO.setBelongTo(belongTo);
		DlxyTitleDTO parent = belongTo;
		if (belongTo.getTitleParentId() == 0)
		{
			parent = titleService.findParentAndHisChilds(belongTo.getTitleId());
		} else
		{
			parent = titleService.findParentAndHisChilds(belongTo.getTitleParentId());
			belongTo.setParent(parent);
		}

		return articleDTO;
	}

	@Transactional
	@Override
	public int rollBackArticle(UserDTO userDTO, int status, Long articleId, int titleId)
	{
		int count = 0;
		try
		{
			count = articleService.rollBackArticle(status, articleId, titleId);
			String detail = "回收站中恢复文章:" + AbstractRecordDetailHandler.ARTICLE + ":" + articleId;
			execute(userDTO, detail);
		} catch (SQLException e)
		{
			throw new RuntimeException("sql错误");
		}
		return count;
	}

	/*
	 * 强业务类型需要加上事务
	 */
	@Transactional
	@Override
	public void insertOrUpdate(ArticleDTO articleDTO)
	{
		/*
		 * 1.文章表插入数据 2.user_article 插入数据 3.修改图片状态
		 */
		articleService.insertOrUpdate(articleDTO);

	}

	@Override
	public void updateArticleByArticleId(ArticleDTO articleDTO) throws SQLException
	{
		articleService.update(articleDTO);
	}

	@Override
	public PageDTO<Collection<ArticleDTO>> findAllArticles(int pageSize, int pageNum)
	{
		Long count = articleService.countAllArticles();
		if (count < 0)
		{
			return PageResultUtil.emptyPage();
		}
		if (pageSize < 1)
		{
			pageSize = 1;
		}
		if (pageNum < 1)
		{
			pageNum = 1;
		}
		Collection<ArticleDTO> collection = articleService.findAllArticlesByPage((pageNum - 1) * pageSize, pageSize);
		return new PageDTO<Collection<ArticleDTO>>(count, collection);
	}

	@Override
	public void addTitleOrUpdate(UserDTO userDTO, DlxyTitleDTO dlxyTitleDTO)
	{
		titleService.insertOrUpdate(dlxyTitleDTO);
		execute(userDTO, "添加或者更新标题:" + AbstractRecordDetailHandler.TITLE + ":" + dlxyTitleDTO.getTitleId());
	}
	@Transactional
	@Override
	public Integer deleteByTitleId(UserDTO userDTO, Integer titleId)
	{
		DlxyTitleDTO dlxyTitleDTO = titleService.findById(titleId);
		if (null == dlxyTitleDTO)
		{
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getRemortIP(request), userDTO.getUserId(),
					"试图删除不存在的标题", IllegalLevelEnum.Suspicious.ordinal());
			throw new DlxySystemIllegalException("试图删除不存在的标题", illegalLogDTO);
		}
		Integer count = titleService.deleteByTitleId(dlxyTitleDTO.getTitleId());
		String detail = "删除标题:" + AbstractRecordDetailHandler.TITLE + ":" + titleId + ":" + dlxyTitleDTO.getTitleName();
		execute(userDTO, detail);
		return count;
	}
	
	@Transactional
	@Override
	public void deleteInBatch(UserDTO userDTO, Long[] articleIds)
	{
		articleService.deleteArticlesInBatch(articleIds);
		// 修改图片状态,或者发布信息,最后还是选择只修改状态,防止瞬间进行io操作 2018-07-09
		// 不需要了,直接数据库自动设置为null即可,因为url中存放着路径
		pictureService.updateArticlePictureStatusByArticleIdsInbatch(articleIds, PictureStatusEnum.Invalid.ordinal());
		// 直接全部删除 采用发布消息的方式
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String realPath = request.getServletContext().getRealPath("imgs");
		for (Long long1 : articleIds)
		{
			System.out.println(realPath + File.separator + long1);
			File file = new File(realPath + File.separator + long1);
			FileUtil.delFileOrDir(file);
		}
		// Collection<PictureDTO> collection =
		// pictureService.findByArticleIdArray(articleIds);
		String detail = "删除文章:" + AbstractRecordDetailHandler.ARTICLE+":";
		for (Long long1 : articleIds)
		{
			detail += long1 + ",";
		}
		execute(userDTO, detail);
	}

	private void execute(UserDTO userDTO, String detail)
	{
		setChanged();
		UserRecordDTO recordDTO = UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), detail);
		recordDTO.valid();
		notifyObservers(recordDTO);
	}

	@Override
	public PageDTO<Collection<ArticleDTO>> findByTitleIds(int pageSize, int pageNum, List<Integer> ids)
			throws SQLException
	{
		Long count = articleCountService.coutArtilcesByTitleIds(ids.toArray(new Integer[ids.size()]));
		if (count <= 0)
		{
			return PageResultUtil.emptyPage();
		} else
		{
			if (pageSize <= 0)
			{
				pageSize = 5;
			}
			if (pageNum <= 0)
			{
				pageNum = 1;
			}
			Collection<ArticleDTO> collection2 = articleService.findArtilcesByTilteIdsAndPage(pageSize, pageNum, ids);
			return new PageDTO<Collection<ArticleDTO>>(count, collection2);
		}
	}

	@Override
	public TitleDetailVO findTitleArticles(int pageSize, int pageNum, int titleId) throws SQLException
	{
		/*
		 * 这个接口需要好好修改逻辑
		 */
		TitleDetailVO titleDetailVO = new TitleDetailVO();
		DlxyTitleDTO dlxyTitleDTO = titleService.findById(titleId);
		DlxyTitleDTO parentTitleDTO = dlxyTitleDTO;
		if (null == dlxyTitleDTO)
		{
			titleDetailVO.setArticlePage(PageResultUtil.emptyPage());
			return titleDetailVO;
		}
		if (pageSize < 1)
		{
			pageSize = 10;
		}
		if (pageNum < 1)
		{
			pageNum = 1;
		}
		int status = ArticleStatusEnum.UP.ordinal();
		Integer tId = dlxyTitleDTO.getTitleId();
		Integer tpId = dlxyTitleDTO.getTitleParentId();
		Integer childKey = tpId;
		List<DlxyTitleDTO> childs = Collections.emptyList();
		Long totalCount = articleCountService.countTitleArticles(tId, tpId, status);
		if (tpId != 0)
		{
			parentTitleDTO = titleService.findById(tpId);
		} else
		{
			childKey = tId;
		}
		childs = (List<DlxyTitleDTO>) titleService.findChildsByParentId(childKey);
		parentTitleDTO.setChilds(childs);
		titleDetailVO.setTitleSelf(dlxyTitleDTO);
		if (totalCount <= 0)
		{
			titleDetailVO.setArticlePage(PageResultUtil.emptyPage());
			titleDetailVO.setParentAndChilds(parentTitleDTO);
			titleDetailVO.setTitleSelf(dlxyTitleDTO);
			return titleDetailVO;
		}

		// 需要的数据:1.顶级父类 2.所有子类 3.所有子类对应的文章
		List<ArticleDTO> collection = new ArrayList<>();
		if (tpId == 0)
		{
			// 说明这个就是一个顶级父类
			collection = (List<ArticleDTO>) articleService.findArticlesByParentTitleId(pageSize, pageNum, tId, status);
			parentTitleDTO = dlxyTitleDTO;
		} else
		{
			// 说明只是一个普通的标题类,有父类,只需要显示自己旗下的文章即可
			collection = (List<ArticleDTO>) articleService.findArticlesByTitleId(pageSize, pageNum, tId, status);
		}

		// dlxyTitleDTO.setArticles(collection);
		// parentTitleDTO.setChilds(childs);
		PageDTO<Collection<ArticleDTO>> pageDTO = new PageDTO<Collection<ArticleDTO>>(totalCount, collection);
		titleDetailVO.setParentAndChilds(parentTitleDTO);
		titleDetailVO.setArticlePage(pageDTO);
		return titleDetailVO;
	}

}
