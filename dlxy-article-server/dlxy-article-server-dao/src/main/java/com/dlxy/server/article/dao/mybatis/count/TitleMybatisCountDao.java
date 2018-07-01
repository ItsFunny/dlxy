/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午4:12:30
* 
*/
package com.dlxy.server.article.dao.mybatis.count;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午4:12:30
*/
@Mapper
public interface TitleMybatisCountDao
{
	@Select("select title_id,title_name,title_parent_id,title_display_seq,create_date from dlxy_title where title_parent_id=#{titleParentId}")
	Long countTitle(int parentTitleId);

}
