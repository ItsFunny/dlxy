/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午3:11:18
* 
*/
package com.management.service;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dlxy.article.server.service.IArticleService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午3:11:18
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {
		"classpath:spring/applicationContext.xml"
})
public class ArticleServiceTest
{
	@Autowired
	private IArticleService articleService;

}
