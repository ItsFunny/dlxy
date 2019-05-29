package com.dlxy.service.command;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.server.article.service.ITitleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.service.IArticleService;

public class ArticleReceiver implements IReceiver
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IArticleService articleServiceImpl;

    @Autowired
    private ITitleService titleService;

    @Override
    public void add(Map<String, Object> map)
    {
        ArticleDTO articleDTO = (ArticleDTO) map.get("articleDTO");
        DlxyTitleDTO title = titleService.findById(articleDTO.getTitleId());
        if (null == title)
        {
            log.error("[add]:title is not exist");
            throw new DlxySystemIllegalException();
        }
        articleDTO.setTitleName(title.getTitleName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        articleDTO.setStartTime(Long.parseLong(sdf.format(c.getTime())));
        articleServiceImpl.insertOrUpdate(articleDTO);
    }

    @Override
    public void update(Map<String, Object> params) throws SQLException
    {
        ArticleDTO articleDTO = (ArticleDTO) params.get("articleDTO");
        articleServiceImpl.update(articleDTO);
    }

    @Override
    public void delete(Map<String, Object> params)
    {
        @SuppressWarnings("unchecked")
        List<Long> articleIds = (List<Long>) params.get("articleIdList");
        articleServiceImpl.deleteArticlesInBatch(articleIds);
    }

}
