/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午10:43:14
* 
*/
package com.dlxy.server.article.dao.mybatis.count;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 上午10:43:14
*/
@Mapper
public interface UserArticleDao
{
	@Insert("insert into dlxy_user_article values (#{userId},#{articleId},#{username})")
	void addUserArticle(@Param("userId")Long userId,@Param("articleId")Long articleId,@Param("username")String username);

}
