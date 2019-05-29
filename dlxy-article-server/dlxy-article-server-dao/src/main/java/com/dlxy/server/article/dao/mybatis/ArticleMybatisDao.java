/**
 * @Description
 * @author joker
 * @date 创建时间：2018年6月28日 上午10:29:02
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
public interface ArticleMybatisDao extends DlxyArticleDao
{
    String UPDATE_STATUS_BY_TITLE_PARENT_ID = "update dlxy_article set article_status =#{articleStatus} where article_id in "
            + "(select article_id from dlxy_article where title_id in "
            + "(select title_id from dlxy_title where title_parent_id =#{titleParentId} ) ) ";

    String FIND_ALL_ARTICLES_BY_TITLE_PARENT_ID = "(" + "SELECT article_id FROM dlxy_article WHERE title_id IN ( "
            + "SELECT title_id FROM dlxy_title WHERE title_parent_id=#{titleParentId})) UNION ALL ( "
            + "SELECT article_id FROM dlxy_article WHERE title_id=#{titleParentId})";

    String FIND_LATEST_ARTICLES = "SELECT a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.article_status,a.create_date,a.update_date  "
            + "FROM dlxy_article a "
            + "WHERE article_status=1 AND EXISTS (SELECT 1 FROM dlxy_title b WHERE b.title_id=a.title_id) "
            + "ORDER BY create_date DESC " + "LIMIT #{count}";

    /*
     * 这条sql只会在显示title_detail的时候,特殊需求而生成的sql语句
     */
    String FIND_ARTICLES_WITH_CONTENT_BY_TITEL_ID = "( "
            + "SELECT a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status,a.article_content FROM dlxy_article a WHERE a.article_status=#{status} AND a.title_id IN ( "
            + "SELECT b.title_id FROM dlxy_title b WHERE b.title_parent_id=#{titleId})) UNION ALL ( "
            + "SELECT c.article_id,c.title_id,c.article_name,c.article_author,c.article_type,c.create_date,c.update_date,c.article_status,c.article_content FROM dlxy_article c WHERE c.article_status=#{status} AND c.title_id=#{titleId} ORDER BY c.create_date DESC)  ";

    @Deprecated
    @Update("update dlxy_article set article_status=#{status} where article_id=#{articleId}")
    void updateArticleStatus(@Param("articleId") Long articleId, @Param("status") Integer status);

    @Deprecated
    void updateStatusInBatch(Map<String, Object> params);

    @Update(UPDATE_STATUS_BY_TITLE_PARENT_ID)
    void updateArticleStatusByTitleParentId(@Param("titleParentId") Integer titleParentId,
                                            @Param("articleStatus") int status);

    Integer updateInBatchSelective(Collection<ArticleDTO> articleDTOs);

    @Insert("insert into dlxy_article (title_name,article_id,title_id,article_name,article_author,article_content,article_status,article_type,start_time ) "
            + "values (#{titleName},#{articleId},#{titleId},#{articleName},#{articleAuthor},#{articleContent},#{articleStatus},#{articleType},#{startTime}) "
            + "on duplicate key update article_status= values(article_status),article_content=values(article_content),article_author=values(article_author),article_type=values(article_type)")
    void insertOrUpdate(ArticleDTO articleDTO);

    /*
     * 先不写吧,不一定要有 查找所有的首页图片推荐文章
     */
    @Deprecated
    Collection<ArticleDTO> findAllPicNews();

    // @Select("select
    // a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date
    // ,a.article_status ,c.realname ,c.user_id,a.delete_date,a.article_content from
    // dlxy_article a where a.article_id=#{articleId}")
    // @Select("select
    // a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date
    // ,a.article_status ,c.realname ,c.user_id,a.delete_date,a.article_content from
    // dlxy_article a,dlxy_title b ,dlxy_user_article c where a.title_id=b.title_id
    // and a.article_id= c.article_id and a.article_id=#{articleId}")
    @Select("select a.article_id,a.visit_count,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date ,a.article_status,a.article_content,c.realname ,c.user_id,c.realname,a.delete_date,b.title_parent_id,b.title_name "
            + "from dlxy_article a left join dlxy_title b  on a.title_id=b.title_id "
            + "left join dlxy_user_article c on a.article_id=c.article_id " + "where 1=1 and "
            + " a.article_id=#{articleId}")
    ArticleDTO findArticleDetailByArticleId(Long articleId);

    @Select(FIND_ALL_ARTICLES_BY_TITLE_PARENT_ID)
    List<ArticleDTO> findAllArticlesByTitleParentId(Integer titleParentId);

    /*
     * 保留 查询某个类目下的所有文章,要分页
     */
    Collection<ArticleDTO> findArticlesInTitleIdsByPage(@Param("start") int start, @Param("end") int end,
                                                        @Param("ids") List<Integer> ids);


    // 保留
    // 查询单个的,为什么要单个,因为可以根据索引查,快,in查询有时候不走索引
    @Select("select "
            + "	a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status"
            + "	,c.realname,c.user_id,a.delete_date,b.title_name from dlxy_article a"
            + "	left join dlxy_title b on a.title_id=b.title_id"
            + "	left join dlxy_user_article c on a.article_id=c.article_id where 1=1" + "	and "
            + "	a.title_id=#{titleId} and a.article_status=#{status} order by a.create_date desc limit #{start},#{end}")
    List<ArticleDTO> findArticlesByTitleId(@Param("start") int start, @Param("end") int end,
                                           @Param("titleId") int titleId, @Param("status") int status);

    // 保留
    @Select("( SELECT a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status FROM dlxy_article a WHERE a.article_status=#{status} AND a.title_id IN ( "
            + "SELECT b.title_id FROM dlxy_title b WHERE b.title_parent_id=#{titleParentId})) UNION ALL ( "
            + "SELECT c.article_id,c.title_id,c.article_name,c.article_author,c.article_type,c.create_date,c.update_date,c.article_status FROM dlxy_article c WHERE c.article_status=#{status} AND c.title_id=#{titleParentId} ORDER BY c.create_date DESC)  limit #{start},#{end}")
    List<ArticleDTO> findArticlesByParentTitleId(@Param("start") int start, @Param("end") int end,
                                                 @Param("titleParentId") int titleParentId, @Param("status") int status);


    @Select(FIND_ARTICLES_WITH_CONTENT_BY_TITEL_ID)
    List<ArticleDTO> findArticlesByTitleIdIWithContent(@Param("titleId") Integer titleId, @Param("status") Integer status);

    // Collection<ArticleDTO>findArticlesInTitleIds(@Param("list")List<Integer>
    // titelIds,@Param("limit")int limit);

    @Deprecated // 用自动生成的代替 取消代替
    @Select("SELECT a.article_id,a.title_id,a.visit_count,(SELECT b.title_parent_id FROM dlxy_title b WHERE b.title_id=a.title_id) titleParentId,a.article_name,a.article_author,a.article_content,a.article_type,a.article_status,a.create_date,a.update_date FROM dlxy_article a WHERE article_id=#{articleId}")
    ArticleDTO findByArticleId(Long articleId);

    Long countArticleByTitleIds(Integer[] ids);

    /**
     * 查询最新的几篇文章
     *
     * @param count
     *            查询的数量
     * @return
     * @author joker
     * @date 创建时间：2018年7月19日 上午7:57:11
     */
    @Select(FIND_LATEST_ARTICLES)
    Collection<ArticleDTO> findLatestArticleLimited(int count);

    /*
     * 找到分类下的所有文章 ,没用了 ,用上述的代替
     */
    @Deprecated
    Collection<ArticleDTO> findByTitleIds(Integer[] ids);

    /*
     * 查询在某段titleid内的 一部分最新的文章
     */
    Collection<ArticleDTO> findArticlesInTitleIdsLimited(@Param("list") List<Integer> ids, @Param("limit") int limit,
                                                         @Param("status") int status);

    // ( SELECT a.article_id,article_name FROM dlxy_article a WHERE EXISTS ( SELECT
    // 1 FROM dlxy_article b WHERE b.article_id=2) AND a.article_id< 2 LIMIT 1)
    // UNION ALL SELECT
    // c.article_id,c.title_id,c.article_name,ç.article_author,c.article_content,ç.article_type,ç.article_status,c.create_date,c.update_date
    // FROM dlxy_article c WHERE c.article_id=2 UNION ALL (SELECT
    // d.article_id,d.article_name FROM dlxy_article d WHERE EXISTS (SELECT 1 FROM
    // dlxy_article e WHERE e.article_id=2) AND d.article_id> 2 LIMIT 1)
    /*
     * 查询文章的上一篇和下一篇
     */
    @Select("(SELECT a.article_id,a.article_name,a.create_date FROM dlxy_article a WHERE a.article_id< #{articleId} AND a.article_status=1 ORDER BY a.article_id DESC LIMIT 1) UNION ALL (SELECT b.article_id,b.article_name,b.create_date FROM dlxy_article b WHERE b.article_id> #{articleId} AND b.article_status=1 ORDER BY b.create_date ASC LIMIT 1)")
    Collection<ArticleDTO> findArticlePrevAndNext(Long articleId);

    @Deprecated
    @Select("select count(1) from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id left join dlxy_user_article c on a.article_id=c.article_id ")
    long countAllArticles();

    /*
     * 这个没用了,准备删除
     */
    @Deprecated
    @Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status ,c.realname,c.user_id,a.delete_date,b.title_name from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id "
            + "left join dlxy_user_article c on a.article_id=c.article_id order by a.create_date desc limit #{start},#{end}")
    Collection<ArticleDTO> findAllArtilcesByPage(@Param("start") int start, @Param("end") int end);

    @Deprecated
    @Select("select a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status ,c.realname,c.user_id,a.delete_date,b.title_name from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id  left join dlxy_user_article c on a.article_id=c.article_id   WHERE article_type= 1 and article_status=1 order by a.create_date desc ")
    Collection<ArticleDTO> findAllRecommedArticles();

    // Collection<ArticleDTO>findArticlesByStatus();

    @Update("update dlxy_article set article_status=#{status} where article_id=#{articleId} and exists(select 1 from dlxy_title b where b.title_id = #{titleId})")
    int rollBackArticle(@Param("status") int status, @Param("articleId") Long articleId,
                        @Param("titleId") Integer titleId);

    @Deprecated
    Integer deleteInBatch(Long[] articleIds);

}
