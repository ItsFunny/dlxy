package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.AbstractDlxyArticleComposite;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.config.DlxyObservervable;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.server.article.service.IArticleCountService;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.AbstractRecordDetailHandler;
import com.dlxy.service.ArticleVisitCountContext;
import com.dlxy.service.IArticleWrappedService;
import com.dlxy.vo.TitleDetailVO;
import com.joker.library.utils.CommonUtils;

public class ArticleWrappedServiceObservableImpl extends DlxyObservervable implements IArticleWrappedService
{

	@Autowired
	private IArticleService articleService;
	@Autowired
	private IArticleCountService articleCountService;

	@Autowired
	private ITitleService titleService;

	@Autowired
	private ArticleVisitCountContext articleVisitCountContext;

	@Override
	public Integer findArticleVisitCount(Long articleId, String ip)
	{
		int result = 0;
		result = articleVisitCountContext.visitCount(articleId);
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
		if (pageSize < 10)
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
	public void updateArticleStatusInBatch(UserDTO userDTO, List<Long> articleIds, int status)
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
	public void updateArticleTypeInBatch(UserDTO userDTO, List<Long> articleIds, int type)
	{
		articleService.updateArticleTypeInbatch(articleIds, type);
		StringBuilder sBuilder = new StringBuilder();
		for (Long long1 : articleIds)
		{
			sBuilder.append(long1 + ",");
		}
		execute(userDTO, "修改文章类型:" + AbstractRecordDetailHandler.ARTICLE + ":" + sBuilder + ":类型-> " + type);
	}
	//
	// @Override
	// public ArticleDTO findArticleDetailByArticleId(Long articleId) throws
	// SQLException
	// {
	//
	// return articleService.findArticleDetailByArticleId(articleId);
	// }

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
	// @Transactional
	// @Override
	// public void insertOrUpdate(ArticleDTO articleDTO)
	// {
	// articleService.insertOrUpdate(articleDTO);
	// }

	@Override
	public void updateArticleByArticleId(ArticleDTO articleDTO) throws SQLException
	{
		articleService.update(articleDTO);
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

	// @Transactional
	// @Override
	// public void deleteInBatch(UserDTO userDTO, List<Long> ids)
	// {
	// // List<Long> ids = Arrays.asList(articleIds);
	// List<Long> backUpdateIds = new ArrayList<>();
	// List<Long> deleteIds = new ArrayList<>();
	// articleService.deleteArticlesInBatch(ids);
	// ServletRequestAttributes attributes = (ServletRequestAttributes)
	// RequestContextHolder.getRequestAttributes();
	// HttpServletRequest request = attributes.getRequest();
	// String realPath = request.getServletContext().getRealPath("imgs");
	// for (int i = ids.size() - 1; i >= 0; i--)
	// {
	// File file = new File(realPath + File.separator + ids.get(i));
	// try
	// {
	// boolean res = FileUtil.delFileOrDir(file);
	// if (res)
	// {
	// deleteIds.add(ids.get(i));
	// } else
	// {
	// backUpdateIds.add(ids.get(i));
	// }
	// } catch (Exception e)
	// {
	// logger.error("[删除文章旗下的图片失败],file:{}", file);
	// backUpdateIds.add(ids.get(i));
	// }
	// }
	// if (!deleteIds.isEmpty())
	// {
	// pictureService.deleteByPictureIds(deleteIds);
	// }
	// if (!backUpdateIds.isEmpty())
	// {
	// pictureService.updateArticlePictureStatusByArticleIdsInbatch(backUpdateIds,
	// PictureStatusEnum.Invalid.ordinal());
	// }
	// String detail = "删除文章:" + AbstractRecordDetailHandler.ARTICLE + ":";
	// for (Long long1 : ids)
	// {
	// detail += long1 + ",";
	// }
	// execute(userDTO, detail);
	// }

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
	public TitleDetailVO findTitleArticlesByTitleId(int pageSize, int pageNum, int titleId) throws SQLException
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

	@Override
	public TitleDetailVO findTitleArticlesByTitleAbbName(int pageSize, int pageNum, String titleAbbName) throws SQLException
	{
		/*
		 * 1.先查询得到自身的所有文章
		 * 2.自身文章不存在,则直接查询子类的
		 * 3.如果自身文章存在,并且数目不足,则子类文章补足 
		 */
		TitleDetailVO titleDetailVO = new TitleDetailVO();
		DlxyTitleDTO dlxyTitleDTO = titleService.findByAbbName(titleAbbName);
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
		 List<ArticleDTO> allArticles = new LinkedList<>();
		if (tpId == 0)
		{
			// 说明这个就是一个顶级父类
			allArticles = (List<ArticleDTO>) articleService.findArticlesByParentTitleId(pageSize, pageNum, tId, status);
			parentTitleDTO = dlxyTitleDTO;
		} else
		{
			// 说明只是一个普通的标题类,有父类,只需要显示自己旗下的文章即可
			allArticles = (List<ArticleDTO>) articleService.findArticlesByTitleId(pageSize, pageNum, tId, status);
		}
//		final  List<ArticleDTO> articleList=allArticles;
//		childs.forEach(c->{  
//			Collection<AbstractDlxyArticleComposite> articles = c.getArticles();
//			 articleList.forEach(a->{
//				if(a.getTitleId().equals(c.getTitleId()))
//				{
//					articles.add(a);
//				}
//			});
//		});
		Iterator<ArticleDTO> iterator = allArticles.iterator();
		while(iterator.hasNext())
		{
			ArticleDTO temp = iterator.next();
			if(temp.getTitleId().equals(dlxyTitleDTO.getTitleId()))
			{
				dlxyTitleDTO.addArticle(temp);
				iterator.remove();
			}
		}
		// dlxyTitleDTO.setArticles(collection);
		 parentTitleDTO.setChilds(childs);
		PageDTO<Collection<ArticleDTO>> pageDTO = new PageDTO<Collection<ArticleDTO>>(totalCount, allArticles);
		titleDetailVO.setParentAndChilds(parentTitleDTO);
		titleDetailVO.setArticlePage(pageDTO);
		return titleDetailVO;
	}

}
