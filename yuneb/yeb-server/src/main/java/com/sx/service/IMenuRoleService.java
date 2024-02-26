package com.sx.service;

import com.sx.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.pojo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface IMenuRoleService extends IService<MenuRole> {

    Result updateMenuRole(Integer rid, Integer[] mids);
}
