package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sx.pojo.MenuRole;
import com.sx.mapper.MenuRoleMapper;
import com.sx.pojo.Result;
import com.sx.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Override
    public Result updateMenuRole(Integer rid, Integer[] mids) {
        QueryWrapper<MenuRole> menuRoleQueryWrapper = new QueryWrapper<>();
        menuRoleQueryWrapper.eq("rid", rid);
        remove(menuRoleQueryWrapper);
        if (mids.length == 0 || mids == null) {
            return Result.ok().message("更新成功");
        } else {
            Integer n = this.baseMapper.insertRecord(rid, mids);
            if (n == mids.length) {
                return Result.ok().message("更新成功");
            } else {
                return Result.error().message("更新失败");
            }
        }
    }
}
