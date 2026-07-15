package com.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    @Select("SELECT * FROM inventory WHERE warehouse_id = #{warehouseId} AND product_id = #{productId}")
    Inventory findByWarehouseAndProduct(Long warehouseId, Long productId);
}