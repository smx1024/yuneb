package com.sx.exception;

import com.sx.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalException {
//    @ExceptionHandler(SQLException.class)
//    public Result sqlException(SQLException  e){
//        if(e instanceof SQLIntegrityConstraintViolationException){
//            return Result.error().message("该数据有关联数据，操作失败！");
//        }
//        return Result.error().message("数据库异常，操作失败！");
//    }

}
