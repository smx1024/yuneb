package com.sx.controller;


import com.sx.pojo.Position;
import com.sx.pojo.Result;
import com.sx.service.IPositionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sx
 * @since 2024-01-12
 */

@RestController
@RequestMapping("system/basic/pos")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @ApiOperation(value = "获取所有职位信息")
    @GetMapping("/")
    public List<Position> getAllPositions(){
        return positionService.list();
    }

    @ApiOperation(value = "添加职位信息")
    @PostMapping("/")
    public Result addPosition(@RequestBody Position position){
        position.setCreateDate(LocalDateTime.now());
        if(positionService.save(position)){
            return Result.ok().message("添加成功！");
        }
        return Result.error().message("添加失败！");
    }

    @ApiOperation(value = "更新职位信息")
    @PutMapping("/")
    public Result updatePosition(@RequestBody Position position){
        System.out.println(position);
            if(positionService.updateById(position)){
            return Result.ok().message("更新成功！");
        }
        return Result.error().message("更新失败！");
    }

    @ApiOperation(value = "删除职位信息")
    @DeleteMapping("/{id}")
    public Result deletePosition(@PathVariable Integer id){
        if(positionService.removeById(id)){
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }

    @ApiOperation(value = "批量删除职位信息")
    @DeleteMapping("/")
    public Result deletePositionByIds(@RequestBody   Integer[] ids){
        if(positionService.removeByIds(Arrays.asList(ids))){
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }

}
