package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFloverMapper {
    /**
     * 插入口味
     * @param flavors
     */
    void insert(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味
     * @param ids
     */
    void delete(List<Long> ids);
    /**
     * 根据菜品id得到口味数据 可能是多种口味 所以使用集合
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 根据菜品id删除口味 单个id删除
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteById(Long id);
}
