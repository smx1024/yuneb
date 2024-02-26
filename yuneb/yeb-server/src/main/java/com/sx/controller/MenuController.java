package com.sx.controller;


import com.sx.pojo.Menu;
import com.sx.service.IAdminService;
import com.sx.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {
    @Autowired
    private IMenuService menuService;
    //通过用户id获取菜单列表
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }
    //test
    @GetMapping("/hello")
    public String hello(){
        List<Menu> menuWithRole = menuService.getMenuWithRole();
        for(Menu menu:menuWithRole){
            String[] str = menu.getRoles().stream().map(role -> role.getName()).toArray(String[]::new);
            for (String s:str){
                System.out.println(s);
            }
        }
        return "hello";
    }
}
