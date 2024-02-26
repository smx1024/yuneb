package com.sx.service.impl;

import com.sx.pojo.Admin;
import com.sx.pojo.Menu;
import com.sx.mapper.MenuMapper;
import com.sx.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Resource
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<Menu> menus = (List<Menu>) redisTemplate.opsForValue().get("menu_" + adminId);
        List<Menu> menuList = menus;
        if (menus == null) {
            menuList = new ArrayList<>();
            //根据用户id查询菜单
            menus = menuMapper.getMenusByAdminId(adminId);
            //一级菜单
            Set<Integer> collect = menus.stream().map(menu -> menu.getParentId()).collect(Collectors.toSet());
            List<Menu> list = listByIds(collect);
            for (Menu menu : list) {
                List<Menu> children = getChildren(menu.getId(), menus);
                menu.setChildren(children);
                menuList.add(menu);
            }
            redisTemplate.opsForValue().set("menu_" + adminId, menuList);
        }
        return menuList;
    }

    @Override
    public List<Menu> getMenuWithRole() {
        return menuMapper.getMenuWithRole();
    }

    @Override
    public List<Menu> getAllMenus() {
        List<Menu> list = list();
        List<Menu> menuList = getChildren(1, list);
        return menuList;
    }

    List<Menu> getChildren(Integer parentId, List<Menu> allMenu) {
        List<Menu> list = allMenu.stream().filter(menu ->
                menu.getParentId() != null && menu.getParentId().equals(parentId)
        ).map(menu -> {
            menu.setChildren(getChildren(menu.getId(), allMenu));
            return menu;
        }).collect(Collectors.toList());
        return list;
    }
}
