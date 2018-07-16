/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 上午10:29:02
* 
*/
package com.dlxy.server.article.dao.mybatis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.context.annotation.Description;

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
public interface ArticleMybatisDao
{

	/**
	 * 查询所有除了删除的文章(article_status=2) ,文章内容这里不查询
	 * 
	 * @return
	 * @author joker
	 * @date 创建时间：2018年6月28日 下午2:22:09
	 */
	// @Select("select
	// a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date
	// ,a.article_status ,b.title_id,b.title_name,c.username "
	// + "from dlxy_article a,dlxy_title b ,dlxy_user_article c where
	// a.title_id=b.title_id and a.article_id= c.article_id and a.article_status <>
	// 2 "
	// + "limit #{start},#{end}")
	// Collection<ArticleDTO> findAllExpectRecommendByPage(@Param("start") int
	// start, @Param("end") int end);

	@Update("update dlxy_article set article_status=#{status} where article_id=#{articleId}")
	void updateArticleStatus(@Param("articleId") Long articleId, @Param("status") Integer status);

	void updateInBatch(Map<String, Object> params);

	@Insert("insert into dlxy_article (article_id,title_id,article_name,article_author,article_content,article_is_recommend,article_status ) "
			+ "values (#{articleId},#{titleId},#{articleName},#{articleAuthor},#{articleContent},#{articleIsRecommend},#{articleStatus}) "
			+ "on duplicate key update article_status= values(article_status),article_content=values(article_content),article_author=values(article_author)")
	void insertOrUpdate(ArticleDTO articleDTO);

	// @Select("select
	// a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date
	// ,a.article_status ,c.username ,c.user_id,a.delete_date,a.article_content from
	// dlxy_article a where a.article_id=#{articleId}")
	// @Select("select
	// a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date
	// ,a.article_status ,c.username ,c.user_id,a.delete_date,a.article_content from
	// dlxy_article a,dlxy_title b ,dlxy_user_article c where a.title_id=b.title_id
	// and a.article_id= c.article_id and a.article_id=#{articleId}")
	@Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date ,a.article_status,a.article_content,c.username ,c.user_id,a.delete_date,b.title_parent_id "
			+ "from dlxy_article a left join dlxy_title b  on a.title_id=b.title_id "
			+ "left join dlxy_user_article c on a.article_id=c.article_id " + "where 1=1 and "
			+ " a.article_id=#{articleId}")
	ArticleDTO findArticleDetailByArticleId(Long articleId);
	
	/*
	 * 查询某个类目下的所有文章,要分页
	 */
	Collection<ArticleDTO>findArticlesInTitleIdsByPage(@Param("start")int start,@Param("end")int end,@Param("ids")List<Integer>ids);
	
	
//	Collection<ArticleDTO>findArticlesInTitleIds(@Param("list")List<Integer> titelIds,@Param("limit")int limit);
	
	
	@Select("select article_id,title_id,article_name,article_author,article_content,article_is_recommend,article_status,create_date,update_date from dlxy_article where article_id =#{articleId}")
	ArticleDTO findByArticleId(Long articleId);
	
	
	Long countArticleByTitleIds(Integer[] ids);
	/*
	 * 找到分类下的所有文章  ,没用了 ,用上述的代替
	 */
	@Description(value = "findArticlesInTitleIds")
	Collection<ArticleDTO> findByTitleIds(Integer[] ids);
	
	

	@Select("select count(1) from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id left join dlxy_user_article c on a.article_id=c.article_id ")
	long countAllArticles();

	/*
	 * 这个没用了,准备删除
	 */
	@Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date,a.article_status ,c.username,c.user_id,a.delete_date,b.title_name from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id "
			+ "left join dlxy_user_article c on a.article_id=c.article_id order by a.create_date desc limit #{start},#{end}")
	Collection<ArticleDTO> findAllArtilcesByPage(@Param("start") int start, @Param("end") int end);

	@Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date,a.article_status ,c.username,c.user_id,a.delete_date,b.title_name from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id  left join dlxy_user_article c on a.article_id=c.article_id   WHERE article_is_recommend= 1 and article_status=1 order by a.create_date desc ")
	Collection<ArticleDTO> findAllRecommedArticles();

	// Collection<ArticleDTO>findArticlesByStatus();

	@Update("update dlxy_article set article_status=#{status} where article_id=#{articleId} and exists(select 1 from dlxy_title b where b.title_id = #{titleId})")
	int rollBackArticle(@Param("status") int status, @Param("articleId") Long articleId,
			@Param("titleId") Integer titleId);

	void deleteInBatch(Long[] articleIds);

}
