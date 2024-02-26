package com.sx.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.pojo.Employee;
import com.sx.pojo.Result;
import com.sx.pojo.Salary;
import com.sx.service.IEmployeeService;
import com.sx.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author AWEI
 * @version 1.0
 * @description: 员工账套
 * @date 2022/3/21 21:03
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value = "获取所有员工账套")
    @GetMapping("/")
    public Result getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                        @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value = "更新员工账套")
    @PutMapping("/")
    public Result updateEmployeeSalary(Integer eid,Integer sid){
        if(employeeService.update(new UpdateWrapper<Employee>().set("salaryId",sid).eq("id",eid))){
            return Result.ok().message("更新成功！");
        }
        return Result.error().message("更新失败！");
    }

}
