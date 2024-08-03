package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺状态接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    private static String KEY = "Shop_Status";
    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result SetShop(@PathVariable Integer status){
        log.info("店铺状态：{}", status == 1 ? "店铺营业1" : "店铺打样1");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }
    @GetMapping("/status")
    @ApiOperation("查询店铺状态操作")
    public Result<Integer> GetShop(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("店铺状态为：{}", status == 1? "店铺营业2" : "店铺打样2");
        return Result.success(status);
    }

}
