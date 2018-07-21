/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 上午8:29:26
* 
*/
package com.dlxy.service;

import java.sql.SQLException;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.vo.ArticleVO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月8日 上午8:29:26
*/
public interface ITitleManagementWrappedService
{
	//最多显示5篇文章(部分)
	Integer MAX_SHOW_ARTICLE_NUMBER=5;
	
	void addParent(DlxyTitleDTO dlxyTitleDTO);
	
	
	//显示dlxy具体的新闻,而不是关于的上方的那些标题
	DlxyTitleDTO findDlxyDetailTitles(Integer limitNumber) throws SQLException;
	
	//查询某个父类下的所有子类和对应的部分文章
	DlxyTitleDTO findChildsAndArticles(Integer titleId,int limitNumber) throws SQLException;
	
	
	
}
