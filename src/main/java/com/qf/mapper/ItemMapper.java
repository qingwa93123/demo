package com.qf.mapper;

import com.qf.entity.Item;
import tk.mybatis.mapper.common.Mapper;

public interface ItemMapper extends Mapper<Item> {

    //根据商品名称分页显示商品信息
    //List<Item> findByItemNameLike(String name);
}
