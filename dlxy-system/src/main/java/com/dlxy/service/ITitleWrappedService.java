package com.dlxy.service;

import java.sql.SQLException;

import com.dlxy.common.dto.DlxyTitleDTO;

public interface ITitleWrappedService
{
	//最多显示5篇文章(部分)
	Integer MAX_SHOW_ARTICLE_NUMBER=5;
	
	void addParent(DlxyTitleDTO dlxyTitleDTO);
	
	
	//显示dlxy具体的新闻,而不是关于的上方的那些标题
	DlxyTitleDTO findDlxyDetailTitles(Integer limitNumber) throws SQLException;
	
	//查询某个父类下的所有子类和对应的部分文章
	DlxyTitleDTO findChildsAndArticles(Integer titleId,int limitNumber) throws SQLException;
	
	
	
}
