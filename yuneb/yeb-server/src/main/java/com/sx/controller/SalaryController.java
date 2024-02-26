package com.sx.controller;


import com.sx.pojo.Result;
import com.sx.pojo.Salary;
import com.sx.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value = "添加工资账套")
    @PostMapping("/")
    public Result addSalary(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if(salaryService.save(salary)){
            return Result.ok().message("添加成功！");
        }
        return Result.error().message("添加失败！");
    }

    @ApiOperation(value = "删除工资账套")
    @DeleteMapping("/{id}")
    public Result deleteSalary(@PathVariable Integer id){
        if(salaryService.removeById(id)){
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }

    @ApiOperation(value = "更新工资账套")
    @PutMapping("/")
    public Result updateSalary(@RequestBody Salary salary){
        if(salaryService.updateById(salary)){
            return Result.ok().message("更新成功！");
        }
        return Result.error().message("更新失败！");
    }

}
