package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")//自己指定bean名字
@RequestMapping("/user/shop")
@Api(tags = "店铺状态接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    private static String KEY = "Shop_Status";
    @GetMapping("/status")
    @ApiOperation("查询店铺状态操作")
    public Result<Integer> GetShop(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("店铺状态为：{}", status == 1 ? "店铺营业" : "店铺打样");
        return Result.success(status);
    }

}
