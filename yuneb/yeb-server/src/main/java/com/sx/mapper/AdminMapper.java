package com.sx.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.pojo.Admin;
import com.sx.pojo.Menu;
import com.sx.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Role> getRoles(Integer adminId);

    List<Admin> getAllAdmins(Integer id, String keywords);
}
