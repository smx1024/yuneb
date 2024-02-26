package com.sx.controller;


import com.sx.pojo.Department;
import com.sx.pojo.Result;
import com.sx.service.IDepartmentService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @ApiModelProperty(value = "获取所有部门")
    @RequestMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }
    @ApiModelProperty(value = "添加部门")
    @PostMapping("/")
    public Result addDep(@RequestBody  Department dep){
        Result result = departmentService.addDep(dep);
        return result;
    }
    @ApiModelProperty(value = "删除部门")
    @DeleteMapping("/{id}")
    public Result deleteDep(@PathVariable Integer id){
        Result result = departmentService.deleteDep(id);
        return result;
    }
}
