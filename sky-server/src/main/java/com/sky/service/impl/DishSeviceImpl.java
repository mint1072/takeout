package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFloverMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishSevice;
import com.sky.vo.DishVO;
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
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
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

    /**
     * 菜品分页操作
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //菜品分页请求参数是在dishPageQueryDTO里 根据需要获取相应数据显示在前端 返回数据除了总类被 还有菜品数据以及关联的分类名
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());//分页插件
        //只需要编写传递给前端的查询语句 但是是针对两张表进行联查 返回类型是page类型
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());//带有总数与前端需要展示的列表数据
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //查看状态是否为起售
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){
                //当前菜品在售，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //查看是否关联套餐表
        for (Long id : ids) {
            List<Long> setmealIds = setmealDishMapper.getById(id);
            if(!setmealIds.isEmpty()) throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //批量删除菜品并删除口味表
        dishMapper.delete(ids);
        dishFloverMapper.delete(ids);

    }
    /**
     * 通过id查询菜品以及口味 并且回显在页面上 属于修改菜品前部分工作
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //菜品和口味在两个数据库中 分两个表操作
        //根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFloverMapper.getByDishId(id);
        //将查询到的数据封装
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品操作 涉及到菜品表和口味表
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        //修改页面涉及到两张表 一张菜品表 一张口味表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);//修改菜品表
        //修改口味表时候避免以往口味数据的混淆 先删除 再插入更新后的口味数据
        //删除口味数据
        dishFloverMapper.deleteById(dish.getId());
        //更新口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();//得到前端传过来的口味集合 将口味存到数据库中 但是口味字段可选
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));//每个口味都标识一下属于哪一个菜品的
        }
        dishFloverMapper.insert(flavors);//插入口味

    }
}
