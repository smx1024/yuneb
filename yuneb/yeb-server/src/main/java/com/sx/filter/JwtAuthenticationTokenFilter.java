package com.sx.filter;


import com.alibaba.fastjson.JSON;
import com.sx.config.JwtTokenUtil;
import com.sx.config.WebUtils;
import com.sx.pojo.Result;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    public RedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = request.getHeader("Authorization");
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录  直接放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtTokenUtil.getClaimsFormToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时  token非法
            //响应告诉前端需要重新登录
            Result result = Result.error().message("需登录后操作");
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        String username = claims.getSubject();
        //从redis中获取用户信息
        UserDetails userDetails= (UserDetails) redisTemplate.opsForValue().get("login" + username);
//        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        //如果获取不到

        if(Objects.isNull(userDetails)){
            //说明登录过期  提示重新登录
            Result result = Result.error().message("需登录后操作");
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
