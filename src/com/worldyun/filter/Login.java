package com.worldyun.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.worldyun.vo.User;

@WebFilter(filterName = "EncoderFilter", urlPatterns = {"*" }, 
            initParams = @WebInitParam(name = "encoder", value = "utf-8")
)
public class Login implements Filter {
    public String[] excludeUrls = new String[]{"/file.html","/getlist","/newdir","/del"};

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servlereq, ServletResponse servleresp, FilterChain chain)throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servlereq;
        HttpServletResponse resp = (HttpServletResponse) servleresp;
        HttpSession session=req.getSession();
        String url = req.getRequestURI();
        User user = (User) session.getAttribute("user");
        Boolean pass = true;
        for (String item : excludeUrls) {
            if (url.indexOf(item) > -1) {
                pass = false;
                break;
            }
        }
        if (!pass) {
            if (user == null) {
                resp.sendRedirect("./index.html");
            }
        }else if ( (url.indexOf("/index.html") > -1 || url.equals("/pan/")) && user != null) {
            resp.sendRedirect("./file.html");
        }
        chain.doFilter(servlereq, servleresp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    
}
