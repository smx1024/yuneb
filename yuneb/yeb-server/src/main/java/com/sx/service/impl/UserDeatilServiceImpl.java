package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sx.pojo.Admin;
import com.sx.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDeatilServiceImpl implements UserDetailsService {
    @Autowired
    private IAdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Admin admin = adminService.getOne(wrapper);
        if (admin == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }
}
