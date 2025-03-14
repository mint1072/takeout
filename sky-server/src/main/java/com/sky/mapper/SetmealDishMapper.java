package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    @Select("select setmeal_id from setmeal_dish where id = #{id}")
    List<Long> getById(Long id);
}
