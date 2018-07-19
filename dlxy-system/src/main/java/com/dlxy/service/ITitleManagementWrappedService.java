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
	void addParent(DlxyTitleDTO dlxyTitleDTO);
	
	
	//显示dlxy具体的新闻,而不是关于的上方的那些标题
	DlxyTitleDTO findDlxyDetailTitles();
	
	//查询某个父类下的所有子类和对应的部分文章
	DlxyTitleDTO findChildsAndArticles(Integer titleId,int limitNumber) throws SQLException;
}
