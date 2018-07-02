/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午10:50:08
* 
*/
package com.dlxy.server.article.service;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 上午10:50:08
*/
public interface IUserArticleService 
{
	void addUserArticle(Long userId,Long articleId,String username);
}
