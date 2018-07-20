/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月20日 下午2:29:45
* 
*/
package com.dlxy.vo;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月20日 下午2:29:45
*/
public class ArticleVO extends ArticleDTO
{
	private DlxyTitleDTO parentAndBros;

	public DlxyTitleDTO getParentAndBros()
	{
		return parentAndBros;
	}

	public void setParentAndBros(DlxyTitleDTO parentAndBros)
	{
		this.parentAndBros = parentAndBros;
	}
}
