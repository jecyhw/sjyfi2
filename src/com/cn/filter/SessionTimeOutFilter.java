package com.cn.filter;

import com.cn.test.TestOutput;
import com.cn.util.Out;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jecyhw on 2015/1/4.
 */
public class SessionTimeOutFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (request.getSession().getAttribute("userName") == null)
        {
            String header = request.getHeader("x-requested-with");
            if (header != null && header.equals("XMLHttpRequest")) {//ajax请求
                response.setHeader("sessionstatus", "timeout");
            } else {
                response.sendRedirect("../index.html");
            }
            return;
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
