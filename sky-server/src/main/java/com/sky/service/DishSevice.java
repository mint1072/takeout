package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishSevice {
    /**
     * 新增菜品操作
     * @param dishDTO
     */
    void insert(DishDTO dishDTO);

    /**
     * 菜品分页操作
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 通过id查询菜品以及口味 并且回显在页面上 属于修改菜品前部分工作
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品操作
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
