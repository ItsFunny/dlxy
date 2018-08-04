package com.dlxy.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.vo.TitleDetailVO;

public interface IArticleWrappedService
{
	// PageDTO<Collection<ArticleDTO>>findArticles(int start,int end,Map<String,
	// Object>params) throws SQLException;

	/*
	 * 查询谋篇文章的访问人数
	 */
	Integer findArticleVisitCount(Long articleId, String ip);


	PageDTO<Collection<ArticleDTO>> findByTitleIds(int pageSize, int pageNum, List<Integer> ids) throws SQLException;

	PageDTO<Collection<ArticleDTO>> findByParams(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException;

	/*
	 * 查询某个分类下的文章 以及显示他的兄弟类或者子类
	 */
	@Deprecated
	TitleDetailVO findTitleArticlesByTitleId(int pageSize, int pageNum, int titleId) throws SQLException;
	
	TitleDetailVO findTitleArticlesByTitleAbbName(int pageSize,int pageNum,String titleAbbName) throws SQLException;

//	ArticleDTO findArticleDetailByArticleId(Long articleId) throws SQLException;

	ArticleDTO showArticleDetail(Long articleId) throws SQLException;
	// void updateArticleStatusInBatch(Long[] articleIds, int status);
	// void delArticle(UserDTO userDTO,Long articleId);

	
	/*
	 * 更新某个类目下所有文章的状态
	 */
	void updateArticleStatusByTitleId(UserDTO userDTO,DlxyTitleDTO titleDTO,int status);
	
	
	void updateArticleStatusInBatch(UserDTO userDTO, List<Long> articleIds, int status);

	void updateArticleTypeInBatch(UserDTO userDTO, List<Long> articleIds, int type);

	void updateArticleByArticleId(ArticleDTO articleId) throws SQLException;

	int rollBackArticle(UserDTO userDTO, int status, Long articleId, int titleId);

//	void insertOrUpdate(ArticleDTO articleDTO);

	//需要更改,会添加业务字段
	void addTitleOrUpdate(UserDTO userDTO, DlxyTitleDTO dlxyTitleDTO);

	Integer deleteByTitleId(UserDTO userDTO, DlxyTitleDTO titleDTO);

//	void deleteInBatch(UserDTO userDTO, List<Long> articleIds);
	
	boolean deleteTitleAndUpdateArticleStatus(UserDTO userDTO,Integer titleId,Integer status);

}
