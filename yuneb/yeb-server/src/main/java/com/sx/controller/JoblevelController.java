package com.sx.controller;


import com.sx.pojo.Joblevel;
import com.sx.pojo.Result;
import com.sx.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    @RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevels(){
        return joblevelService.list();
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public Result addJobLevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if (joblevelService.save(joblevel)) {
            return Result.ok().message("添加成功！");
        }
        return Result.error().message("添加失败！");
    }


    @ApiOperation(value = "更新职称信息")
    @PutMapping("/")
    public Result updateJobLevel(@RequestBody Joblevel joblevel){
        if(joblevelService.updateById(joblevel)){
            return Result.ok().message("更新成功！");
        }
        return Result.error().message("更新失败！");
    }

    @ApiOperation(value = "删除职称信息")
    @DeleteMapping("/{id}")
    public Result deleteJobLevel(@PathVariable Integer id){
        if(joblevelService.removeById(id)){
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }

    @ApiOperation(value = "批量删除职称信息")
    @DeleteMapping("/")
    public Result deleteJobLevelsByIds(@RequestBody  Integer[] ids){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }


}
