package vkb.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SwaggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if ("".equals(request.getRequestURI()) || "/".equals(request.getRequestURI()) ||
            request.getContextPath().equals(request.getRequestURI()) ||
                        (request.getContextPath()+"/").equals(request.getRequestURI()


                )){
            response.sendRedirect(request.getContextPath()+"/swagger-ui.html");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


}