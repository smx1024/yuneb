package com.sx.service;

import com.sx.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.pojo.Result;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    Result addDep(Department dep);

    Result deleteDep(Integer id);
}
