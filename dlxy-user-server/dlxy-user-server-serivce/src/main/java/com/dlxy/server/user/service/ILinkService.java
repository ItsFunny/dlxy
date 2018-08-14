/**
*
* @author joker 
* @date 创建时间：2018年8月13日 上午11:17:23
* 
*/
package com.dlxy.server.user.service;

import java.util.List;

import com.dlxy.server.user.model.DlxyLink;

/**
* 
* @author joker 
* @date 创建时间：2018年8月13日 上午11:17:23
*/
public interface ILinkService
{
	List<DlxyLink> findAllLinks();
	
	Integer addOrUpdate(DlxyLink dlxyLink);
	
	Integer deleteByLinkId(Integer linkId);
	
	DlxyLink findById(Integer linkId);
}
