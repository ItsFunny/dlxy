/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 上午8:11:03
* 
*/
package com.dlxy.server.user.dao.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 上午8:11:03
*/
@Mapper
public interface UserIllegalLogDao
{
	@Insert("insert into dlxy_user_illegal_log  (user_id,illegal_detail,illegal_level) values (#{userId},#{illegalDetail},#{illegalLevel})")
	void addLogDetail(@Param("userId")Long userId,@Param("illegalDetail")String illegalDetail,@Param("illegalLevel")int illegalLevel);
}
