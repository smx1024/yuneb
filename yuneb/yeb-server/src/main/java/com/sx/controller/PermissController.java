package com.sx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sx.pojo.Menu;
import com.sx.pojo.MenuRole;
import com.sx.pojo.Result;
import com.sx.pojo.Role;
import com.sx.service.IMenuRoleService;
import com.sx.service.IMenuService;
import com.sx.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AWEI
 * @version 1.0
 * @description: 权限组
 * @date 2022/3/18 16:34
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public Result addRole(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if(roleService.save(role)){
            return Result.ok().message("添加成功！");
        }
        return Result.error().message("添加失败！");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public Result deleteRole(@PathVariable Integer rid){
        if(roleService.removeById(rid)){
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid))
                .stream().map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public Result updateMenuRole(Integer rid,@RequestBody Integer[] mids){
        System.out.println(rid);
        System.out.println(Arrays.toString(mids));
        return menuRoleService.updateMenuRole(rid,mids);
    }

}
