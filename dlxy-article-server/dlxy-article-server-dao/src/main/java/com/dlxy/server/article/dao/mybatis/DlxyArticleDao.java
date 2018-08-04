package com.dlxy.server.article.dao.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.model.DlxyArticleExample;

public interface DlxyArticleDao {
    long countByExample(DlxyArticleExample example);

    int deleteByExample(DlxyArticleExample example);

    int deleteByPrimaryKey(Long articleId);

    int insert(ArticleDTO record);

    int insertSelective(ArticleDTO record);

    List<ArticleDTO> selectByExample(DlxyArticleExample example);

    ArticleDTO selectByPrimaryKey(Long articleId);

    int updateByExampleSelective(@Param("record") ArticleDTO record, @Param("example") DlxyArticleExample example);

    int updateByExample(@Param("record") ArticleDTO record, @Param("example") DlxyArticleExample example);

    int updateByPrimaryKeySelective(ArticleDTO record);

    int updateByPrimaryKey(ArticleDTO record);
}