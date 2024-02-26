package com.sx.mapper;

import com.sx.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    Integer insertRecord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
