package com.sx.controller;


import com.sx.pojo.Admin;
import com.sx.pojo.Result;
import com.sx.service.IAdminService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @ApiModelProperty(value = "获取所有操作员")
    @GetMapping("")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }
    @ApiModelProperty(value = "更新操作员")
    @PutMapping("")
    public Result updateAdmin(Admin admin){
        if( adminService.updateById(admin)){
            return  Result.ok().message("更新成功");
        }
        return Result.error().message("更新失败");
    }
    @ApiModelProperty(value = "删除操作员")
    @DeleteMapping("/{id}")
    public Result deleteAdmin(@PathVariable Integer id){
        if( adminService.removeById(id)){
            return  Result.ok().message("删除成功");
        }
        return Result.error().message("删除失败");
    }
    @ApiModelProperty(value = "更新操作员角色")
    @PutMapping("/role")
    public Result updateAdminRole(Integer adminId,Integer[] rids){
        return adminService.updateAdminRole(adminId,rids);
    }

}
