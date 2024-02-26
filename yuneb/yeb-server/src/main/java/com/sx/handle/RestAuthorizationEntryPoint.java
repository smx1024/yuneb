package com.sx.handle;

import com.alibaba.fastjson.JSON;
import com.sx.config.WebUtils;
import com.sx.pojo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //没有登录
        Result result = Result.error().code(401).message("请登录");
        WebUtils.renderString(response, JSON.toJSONString(result));

    }
}
