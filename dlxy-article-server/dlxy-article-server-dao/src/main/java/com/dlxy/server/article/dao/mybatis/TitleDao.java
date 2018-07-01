/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:15:06
* 
*/
package com.dlxy.server.article.dao.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.dlxy.common.dto.DlxyTitleDTO;


/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午3:15:06
*/
@Mapper
public interface TitleDao
{
	/*
	 * 可做缓存
	 */
	@Select("select title_id,title_name,title_parent_id,title_display_seq,create_date from dlxy_title where title_parent_id=#{titleParentId}")
	Collection<DlxyTitleDTO> findAllParent(int titleParentId);

}
