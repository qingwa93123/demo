package com.qf.service;

import com.qf.entity.User;

public interface UserService {

    //检验用户名是否可用
    void checkUsername(String username);

    //用户注册
    void register(User user);

    //用户登录
    User login(String username,String password);
}
