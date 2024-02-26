package com.sx.service.impl;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sx.config.StringMessagePostProcessor;
import com.sx.mapper.MailLogMapper;
import com.sx.pojo.Employee;
import com.sx.mapper.EmployeeMapper;
import com.sx.pojo.MailConstants;
import com.sx.pojo.MailLog;
import com.sx.pojo.Result;
import com.sx.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RabbitTemplate  rabbitTemplate;
    @Autowired
    private MailLogMapper mailLogMapper;
    @Override
    public Result getEmployee(Integer currentPage, Integer pageSize, Employee employee, LocalDate[] beginDateScope) {
        Page<Employee> page = new Page<>(currentPage, pageSize);
        IPage<Employee> page1= employeeMapper.getEmployeePage(page, employee, beginDateScope);
        return Result.ok().data("total", page1.getTotal()).data("data", page1.getRecords());
    }

    @Override
    public Result maxWorkID() {
        List<Map<String, Object>> list = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        Map<String, Object> map = list.get(0);
        Integer integer = Integer.valueOf(map.get("max(workID)").toString());
        return Result.ok().data("maxWorkID", integer+1);
    }

    @Override
    public Result addEmp(Employee employee) {
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = endContract.toEpochDay() - beginContract.toEpochDay();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));
        if (employeeMapper.insert(employee) == 1) {
            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);

            //数据库记录发送的信息
            String msgId = UUID.randomUUID().toString();
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setEid(employee.getId());
            mailLog.setStatus(0);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDate.now());
            mailLog.setCreateTime(LocalDate.now());
            mailLog.setUpdateTime(LocalDate.now());
            mailLogMapper.insert(mailLog);
            //发送消息
            Message<Employee> message = MessageBuilder.withPayload(emp).setHeader("msgid", msgId).build();
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME,message,new CorrelationData(msgId));
            return Result.ok().message("添加成功");
        }
        return Result.error().message("添加失败");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);

    }

    @Override
    public Result getEmployeeWithSalary(Integer currentPage, Integer size) {
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> page1 = employeeMapper.getEmployeeWithSalary(page);
        return Result.ok().data("total", page1.getTotal()).data("data", page1.getRecords());
    }
}
