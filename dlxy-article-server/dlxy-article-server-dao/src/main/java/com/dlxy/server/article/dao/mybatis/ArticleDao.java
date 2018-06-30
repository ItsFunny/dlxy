/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 上午10:29:02
* 
*/
package com.dlxy.server.article.dao.mybatis;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dlxy.common.dto.ArticleDTO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 上午10:29:02
 */
@Mapper
public interface ArticleDao
{

	/**
	 * 查询所有除了删除的文章(article_status=2) ,文章内容这里不查询
	 * @return
	 * @author joker
	 * @date 创建时间：2018年6月28日 下午2:22:09
	 */
	@Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date ,a.article_status ,b.title_id,b.title_name,c.username "
			+ "from  dlxy_article  a,dlxy_title b ,dlxy_user_article c where a.title_id=b.title_id and a.article_id= c.article_id and a.article_status <> 2 "
			+ "limit #{start},#{end}")
	Collection<ArticleDTO> findAllExpectRecommendByPage(@Param("start") int start, @Param("end") int end);

	@Update("update dlxy_article set article_status=#{status} where article_id=#{articleId}")
	void updateArticleStatus(@Param("articleId") Long articleId, @Param("status") Integer status);
	
	
	void updateInBatch(Map<String, Object>params);
}
