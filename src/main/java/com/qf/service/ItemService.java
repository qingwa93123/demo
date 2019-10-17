package com.qf.service;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Item;

public interface ItemService {

    //根据商品名称分页显示商品信息
    PageInfo<Item> findByItemName(String name, Integer page, Integer size);

    //添加商品信息
    void save(Item item);

    //删除商品信息
    void delete(Integer id);

    //修改商品信息
    void update(Item item);

    //根据商品id查询商品信息
    Item findByItemId(Integer id);
}
