/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月30日 上午9:36:17
* 
*/
package com.dlxy.system.management.test.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dlxy.server.article.dao.mybatis.ArticleDao;
import com.dlxy.system.management.config.ManagementRabbitMQConfiguration;
import com.dlxy.system.management.config.ManagementSystemConfiguration;
import com.dlxy.system.management.config.property.DlxyProperty;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月30日 上午9:36:17
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes= {
				ManagementRabbitMQConfiguration.class,ManagementSystemConfiguration.class
		}
)
public class ArticleDaoTest
{
	@Test
	public void testUpdateInBatch()
	{
		ApplicationContext context=new AnnotationConfigApplicationContext(ManagementSystemConfiguration.class,DlxyProperty.class);
		ArticleDao articleDao = context.getBean(ArticleDao.class);
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("ids", new Long[] {1l,2l});
		params.put("status", 3);
		articleDao.updateInBatch(params);
		
	}
}
