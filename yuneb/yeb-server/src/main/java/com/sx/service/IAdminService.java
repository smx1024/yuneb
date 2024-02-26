package com.sx.service;

import com.sx.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.pojo.Menu;
import com.sx.pojo.Result;
import com.sx.pojo.Role;
import com.sx.vo.AdminVo;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface IAdminService extends IService<Admin> {


    Result login(AdminVo adminVo, HttpServletRequest request);

    Admin getInfo(String username);

    List<Role> getRoles (Integer adminId);

    List<Admin> getAllAdmins(String keywords);

    Result updateAdminRole(Integer adminId, Integer[] rids);

    Result updateAdminPassword(String oldPass, String pass, Integer adminId);

    Result updateAdminUserFace(MultipartFile file, Integer id, Authentication authentication);
}
