package com.sx.service.impl;

import com.sx.pojo.Department;
import com.sx.mapper.DepartmentMapper;
import com.sx.pojo.Result;
import com.sx.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;
    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    public Result addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if(1==dep.getResult()){
            return Result.ok().message("添加成功").data("dep",dep);
        }
        return Result.error().message("添加失败");
    }

    @Override
    public Result deleteDep(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.deleteDep(dep);
        if(-2==dep.getResult()){
            return Result.error().message("该部门下有子部门，删除失败");
        }else if(-1==dep.getResult()){
            return Result.error().message("该部门下有员工，删除失败");
        }else if(1==dep.getResult()){
            return Result.ok().message("删除成功");
        }
        return Result.error().message("删除失败");
    }
}
