package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFloverMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishSevice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品操作
 */
@Service
public class DishSeviceImpl implements DishSevice {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFloverMapper dishFloverMapper;
    @Transactional
    @Override
    public void insert(DishDTO dishDTO) {
        /**
         * 分为两部操作
         */
        //菜品相关信息插入菜品表
        Dish dish = new Dish();//生成dish实体对象 dish作为数据库字段映射 没有口味字段 口味字段在另一张表 所以针对两个Mapper进行操作
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();//得到插入菜品语句回传回来的id
        //口味相关信息插入口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();//得到前端传过来的口味集合 将口味存到数据库中 但是口味字段可选
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));//每个口味都标识一下属于哪一个菜品的
        }
        dishFloverMapper.insert(flavors);//插入口味
    }
}
