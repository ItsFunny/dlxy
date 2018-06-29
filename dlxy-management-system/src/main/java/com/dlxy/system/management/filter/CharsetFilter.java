/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午8:36:29
* 
*/
package com.dlxy.system.management.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.expression.ConstructorResolver;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午8:36:29
*/
public class CharsetFilter implements Filter
{
	private String encoding;
	@Override
	public void destroy()
	{
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException
	{
		HttpServletResponse response=(HttpServletResponse) arg1;
		if(null!=encoding)
		{
			response.setContentType("text/html;charset="+encoding);
			response.setCharacterEncoding(encoding);
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		this.encoding=arg0.getInitParameter("encoding");
	}

}
