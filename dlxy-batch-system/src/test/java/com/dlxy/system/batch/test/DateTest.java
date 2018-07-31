/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月31日 下午1:55:15
* 
*/
package com.dlxy.system.batch.test;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月31日 下午1:55:15
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DateTest
{
	@Autowired
	private QueryRunner queryRunner;

	@Test
	public void init() throws SQLException
	{
		queryRunner.getDataSource().getConnection().setAutoCommit(false);
		// String sql="insert into t (date,a) values (?,?)";

		List<Object> l = new LinkedList<Object>();
		System.out.println("begin insert");
		Long beginTime = System.currentTimeMillis();
		for (int i = 0; i < 200000; i++)
		{
			l.clear();
			String sql = "insert into t (date,a) values (?,?)";
			l.add(new Date());
			l.add(":" + i);
			Object query = queryRunner.insert(sql, new ScalarHandler<Object>(), l.toArray());
		}
		System.out.println("end insert ,consume:" + (System.currentTimeMillis() - beginTime));
	}

	@Test
	public void testTimeStamp() throws SQLException
	{
		System.out.println(System.currentTimeMillis());
		String sql = "select count(1) from t where UNIX_TIMESTAMP(date)< ? ";

		long begin = System.currentTimeMillis();

		Object query = queryRunner.query(sql, new ScalarHandler<Object>(), System.currentTimeMillis());

		System.out.println(
				"end ,queryCount:" + ((Number) query).longValue() + "consume:" + (System.currentTimeMillis() - begin));
	}

	@Test
	public void testDate() throws SQLException
	{
		String sql = "select count(1) from t where datediff(SECOND,date,CURRENT_DATE)>10";

		long begin = System.currentTimeMillis();
//		Object query = queryRunner.query(sql, new ScalarHandler<Object>(),null);
		Object query = queryRunner.query(sql, new ScalarHandler<Object>());
		System.out.println(
				"end ,queryCount:" + ((Number) query).longValue() + "consume:" + (System.currentTimeMillis() - begin));
	}

}
