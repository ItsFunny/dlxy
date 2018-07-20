/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月15日 下午6:03:10
* 
*/
package com.dlxy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月15日 下午6:03:10
*/
public class CharsetFilter implements Filter
{

	private String encoding=null;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.encoding=filterConfig.getInitParameter("encoding");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		if(null!=encoding)
		{
			request.setCharacterEncoding(encoding);
			response.setContentType("text/html;charset="+encoding);
			response.setCharacterEncoding(encoding);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		
	}

}