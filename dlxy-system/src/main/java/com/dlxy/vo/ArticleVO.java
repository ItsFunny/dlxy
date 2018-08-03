package com.dlxy.vo;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;

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
