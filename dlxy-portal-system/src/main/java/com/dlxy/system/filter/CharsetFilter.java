/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午7:00:20
* 
*/
package com.dlxy.system.filter;

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
* @date 创建时间：2018年7月9日 下午7:00:20
*/
public class CharsetFilter implements Filter
{
	private String encoding=null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		encoding=filterConfig.getInitParameter("encoding");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		if(null!=encoding)
		{
			response.setContentType("text/html;charset="+encoding);
			request.setCharacterEncoding(encoding);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

}
