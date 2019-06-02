package com.dlxy.filter;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.utils.AdminUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2019-05-29 16:23
 */
public class AdminFilter implements Filter
{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURL = request.getRequestURL().toString().toLowerCase();
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (requestURL.toLowerCase().contains("admin"))
        {
            UserDTO loginUser = AdminUtil.getLoginUser();
            if (null == loginUser)
            {
                response.sendRedirect("/admin/login.html?error=请先登录");
                return;
            } else
            {
                if (requestURL.contains("rest") || requestURL.contains("v1"))
                {
                    if (!loginUser.isAdmin())
                    {
                        response.getWriter().write("无权限进行相关操作");
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy()
    {

    }
}
