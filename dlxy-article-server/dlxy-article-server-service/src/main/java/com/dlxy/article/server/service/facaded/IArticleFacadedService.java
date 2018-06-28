/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:29:52
* 
*/
package com.dlxy.article.server.service.facaded;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;


import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午11:29:52
*/
public interface IArticleFacadedService
{
	PageDTO<Collection<ArticleDTO>>findArticles(int pageSize,int pageNum,Map<String, String>params) throws SQLException;
}
