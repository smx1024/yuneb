package com.sx.service;

import com.sx.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.pojo.Result;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
public interface IEmployeeService extends IService<Employee> {

    Result getEmployee(Integer currentPage, Integer pageSize, Employee employee, LocalDate[] beginDateScope);

    Result maxWorkID();

    Result addEmp(Employee employee);

    List<Employee> getEmployee(Integer id);

    Result getEmployeeWithSalary(Integer currentPage, Integer size);
}
