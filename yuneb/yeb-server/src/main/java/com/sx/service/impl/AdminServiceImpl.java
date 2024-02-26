package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sx.config.JwtTokenUtil;
import com.sx.config.OssUtils;
import com.sx.mapper.AdminRoleMapper;
import com.sx.pojo.*;
import com.sx.mapper.AdminMapper;
import com.sx.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.vo.AdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Result login(AdminVo adminVo, HttpServletRequest request) {
        String code = (String) request.getSession().getAttribute("code");
//        if (Objects.isNull(code)) {
//          return Result.error().message("验证码错误");
//        }
        UsernamePasswordAuthenticationToken authenticationToken1 = new UsernamePasswordAuthenticationToken(adminVo.getUsername(), adminVo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken1);
        if (Objects.isNull(authenticate)) {
            return Result.error().message("用户名或密码错误");
        }
        Admin admin = (Admin) authenticate.getPrincipal();
        String username = admin.getUsername();
        redisTemplate.opsForValue().set("login" + username, admin);
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //将用户信息存入到security全局上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成token
        String jwtToken = JwtTokenUtil.generateToken(userDetails);

        return Result.ok().data("token", jwtToken);
    }

    @Override
    public Admin getInfo(String username) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", username);
        Admin admin = baseMapper.selectOne(adminQueryWrapper);
        if (admin == null) {
            throw new RuntimeException("用户名不存在");
        }
        admin.setPassword(null);
        admin.setRoles(adminMapper.getRoles(admin.getId()));
        return admin;
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return adminMapper.getRoles(adminId);
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = admin.getId();
        return adminMapper.getAllAdmins(id, keywords);

    }

    //更新操作员角色
    @Override
    public Result updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));
        Integer n = adminRoleMapper.addByAdminId(adminId, rids);
        if (n == rids.length) {
            return Result.ok().message("更新成功");
        }
        return Result.error().message("更新失败");
    }

    @Override
    public Result updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = getById(adminId);
        if (passwordEncoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(pass));
            boolean b = updateById(admin);
            if (b) {
                return Result.ok().message("更新成功");
            }
        }
        return Result.error().message("更新失败");
    }

    @Override
    public Result updateAdminUserFace(MultipartFile file, Integer id, Authentication authentication) {
        Admin admin = getById(id);
        if (admin == null) {
            return Result.error().message("用户不存在");
        }
        String url = OssUtils.upload(file);
        admin.setUserFace(url);
        boolean b = updateById(admin);
        if (b) {
            return Result.ok().data("url",url).message("更新成功");
        }
        return Result.error().message("更新失败");

    }


}
