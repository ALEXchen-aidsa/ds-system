package com.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}