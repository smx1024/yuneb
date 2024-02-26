package com.sx.controller;


import com.sx.pojo.Admin;
import com.sx.pojo.Result;
import com.sx.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author AWEI
 * @version 1.0
 * @description: 个人中心
 * @date 2022/3/27 20:36
 */
@RestController
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;


    @GetMapping("/admin/info")
    public Admin getAdminInfo(Authentication authentication){
        Admin principal = (Admin) authentication.getPrincipal();
        Integer id = principal.getId();
        return adminService.getById(id);
    }

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public Result updateAdmin(@RequestBody Admin admin, Authentication authentication){
        admin.setEnabled(true);
        if(adminService.updateById(admin)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return Result.ok().message("更新成功！");
        }
        return Result.error().message("更新失败！");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public Result updateAdminPasswors(@RequestBody Map<String,Object> info){
        String oldPass = (String)info.get("oldpass");
        String pass = (String)info.get("pass");
        Integer adminId = (Integer)info.get("adminId");
        return adminService.updateAdminPassword(oldPass,pass,adminId);
    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("/admin/userface")
    public Result updateAdminUserFace( MultipartFile file,Integer id,Authentication authentication){
     Result result=adminService.updateAdminUserFace(file,id,authentication);
     return  result;
    }

}
