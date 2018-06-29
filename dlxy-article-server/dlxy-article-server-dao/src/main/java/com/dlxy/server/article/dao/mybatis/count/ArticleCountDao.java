/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 上午10:29:24
* 
*/
package com.dlxy.server.article.dao.mybatis.count;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* 
* @When ever u want
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 上午10:29:24
*/
@Mapper
public interface ArticleCountDao
{
	@Select("select count(1) from dlxy_article where article_is_recommend =0 ")
	Long coutAllExceptRecommend();
	
	
}
