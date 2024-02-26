package com.sx.controller;

import com.sx.pojo.Admin;
import com.sx.pojo.Result;
import com.sx.service.IAdminService;
import com.sx.service.impl.AdminServiceImpl;
import com.sx.vo.AdminVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
@Api(tags = "LoginController")
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @ApiModelProperty("登录")
    @PostMapping("/login")
    public Result login(@RequestBody AdminVo adminVo, HttpServletRequest request){
        return  adminService.login(adminVo, request);

    }
    @PostMapping("/test")
    public Result test(@RequestBody String username){
        System.out.println(username);
        return Result.ok();
    }

    //获取用户信息
    @ApiModelProperty("获取用户信息")
    @GetMapping("/info")
    public Result info(Principal principal ) {
//        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getName();
        Admin details = adminService.getInfo(username);
        details.setPassword(null);
        details.setRoles(adminService.getRoles(details.getId()));
        return Result.ok().data("details",details);
    }
    //注销
    @ApiModelProperty("注销")
    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }
}
