/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 上午10:29:02
* 
*/
package com.dlxy.article.server.mybatis;

import java.util.Collection;

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
	 * 查询所有文章,除了推荐的文章之外 ,文章内容不查询
	 * @return 
	 * @author joker 
	 * @date 创建时间：2018年6月28日 下午2:22:09
	 */
	@Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date ,a.article_status ,b.title_id,b.title_name"
			+ " from dlxy_article a left join dlxy_title b on a.title_id=b.title_id "
			+ "limit #{start},#{end}")
	Collection<ArticleDTO> findAllExpectRecommendByPage(@Param("start")int start,@Param("end")int end);
	
	@Update("update dlxy_article set article_status=#{status} where article_id=#{articleId}")
	void updateArticleStatus(@Param("articleId")Long articleId,@Param("status")Integer status);
}
