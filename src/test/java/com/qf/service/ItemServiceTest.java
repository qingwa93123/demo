package com.qf.service;

import com.github.pagehelper.PageInfo;
import com.qf.SpringTests;
import com.qf.entity.Item;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ItemServiceTest extends SpringTests {

    @Autowired
    private ItemService itemService;

    @Test
    public void findByItemName() {
        PageInfo<Item> pageInfo = itemService.findByItemName(null, 1, 8);
        List<Item> list = pageInfo.getList();
        for (Item item : list) {
            System.out.println(item);
        }
    }
}