package com.sx.mapper;

import com.sx.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * @return
     * @description: 获取所有部门
     * @param:
     */
    List<Department> getAllDepartments(Integer parentId);

    /**
     * @return
     * @description: 添加部门
     * @param: dep
     */
    void addDep(Department dep);

    /**
     * @return
     * @description: 删除部门
     * @param: dep
     */
    void deleteDep(Department dep);


}
