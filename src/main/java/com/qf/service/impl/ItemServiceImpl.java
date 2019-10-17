package com.qf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.entity.Item;
import com.qf.enums.ExceptionEnum;
import com.qf.exception.SsmException;
import com.qf.mapper.ItemMapper;
import com.qf.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    public PageInfo<Item> findByItemName(String name, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Item.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name",  "%" + name + "%");
        }
        List<Item> itemList = itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
        return pageInfo;
    }

    @Override
    @Transactional
    public void save(Item item) {
        int count = itemMapper.insertSelective(item);
        if (count!=1){
            log.error("【添加商品】 添加商品失败!  item = {}",item);
            throw new SsmException(ExceptionEnum.ITEM_ADD_ERROR);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        int count = itemMapper.deleteByPrimaryKey(id);
        if(count != 1){
            log.error("【删除商品】 删除商品失败!  id = {}",id);
            throw new SsmException(ExceptionEnum.ITEM_DELETE_ERROR);
        }
    }

    @Override
    @Transactional
    public void update(Item item) {
        int count = itemMapper.updateByPrimaryKeySelective(item);
        if (count!=1){
            log.error("【修改商品】 修改商品失败!  item = {}",item);
            throw new SsmException(ExceptionEnum.ITEM_UPDATE_ERROR);
        }
    }


    @Override
    public Item findByItemId(Integer id) {
        Item item = itemMapper.selectByPrimaryKey(id);
        if (item==null){
            log.error("【查询商品】 查询商品失败!  id = {}",id);
            throw new SsmException(ExceptionEnum.ITEM_SELECT_ERROR);
        }
        return item;
    }
}
