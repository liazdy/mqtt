package com.gyjs.mqtt.admin.security;


import com.gyjs.mqtt.admin.Utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * JWT过滤器，拦截 /secure的请求
 */
@Slf4j
@Component
@WebFilter(filterName = "JwtFilter", urlPatterns = "/*")
public class JwtFilter implements Filter {
    // 设置排除路径
    private final List<String> excludeUrls = Collections.singletonList("/user/login");

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        response.setCharacterEncoding("UTF-8");
        //获取 header里的token
        final String token = request.getHeader("authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }
        // Except OPTIONS, other request should be checked by JWT
        else {
            String requestURI = request.getRequestURI();

            if (excludeUrls.contains(requestURI)) {
                chain.doFilter(req, res);
                return;
            }

            if (token == null || token.isEmpty()) {
                response.getWriter().write("没有token！");
                return;
            }

            if (!JWTUtil.verify(token) || JWTUtil.getInfo(token).get("username") == null || !"admin".equalsIgnoreCase((String) JWTUtil.getInfo(token).get("username"))) {
                response.getWriter().write("token不合法！");
                return;
            }
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }
}
