package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishSevice;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishSevice.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("菜品的批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品的批量删除：{}",ids);
        dishSevice.delete(ids);

        //将所有的菜品缓存数据全部清理掉,所有以dish_开头的key
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
//        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品：{}",id);
        DishVO dishVO = dishSevice.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);
        dishSevice.updateWithFlavor(dishDTO);

        //将所有的菜品缓存数据全部清理掉,所有以dish_开头的key
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
//        cleanCache("dish_*");

        return Result.success();
    }

}
