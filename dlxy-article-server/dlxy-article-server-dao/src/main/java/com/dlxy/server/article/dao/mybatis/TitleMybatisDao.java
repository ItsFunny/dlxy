/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:15:06
* 
*/
package com.dlxy.server.article.dao.mybatis;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
public interface TitleMybatisDao
{
	
	Long countTilte(Map<String, Object>params);
	/*
	 * 可做缓存
	 */
	@Select("select title_id,title_name,title_parent_id,title_display_seq,create_date from dlxy_title where title_parent_id=#{titleParentId} and title_id <> 0")
	Collection<DlxyTitleDTO> findParentAllChilds(int titleParentId);
	
	@Select("select title_id,title_name,title_parent_id,title_display_seq,create_date from dlxy_title where title_parent_id= 0")
	Collection<DlxyTitleDTO>findAllParents();
	
	@Select("select title_id,title_name,title_parent_id,title_display_seq,create_date from dlxy_title where title_id= #{id}")
	DlxyTitleDTO findById(Integer id);
	
	void insertOrUpdate(DlxyTitleDTO dlxyTitleDTO);
	
	@Delete("delete from dlxy_title where title_id <> 0 and title_id =#{titleId} ")
	void deleteByTitleId(Integer titleId);
}
