package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishSevice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishSevice dishSevice;
    @PostMapping
    @ApiOperation("新增菜品操作")
    public Result save(@RequestBody DishDTO dishDTO){
        /**
         * 新增菜品操作又分为新增菜品和口味操作 所以是两个Mapper操作
         * 在新增口味操作时候涉及到针对哪个菜品进行口味添加 所以涉及到菜品id 需要回传主键操作 不然你插入菜品id时候没有值
         */
        dishSevice.insert(dishDTO);
        return Result.success();
    }
}
