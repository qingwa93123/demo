package com.qf.service.impl;


import com.qf.entity.User;
import com.qf.enums.ExceptionEnum;
import com.qf.exception.SsmException;
import com.qf.mapper.UserMapper;
import com.qf.service.UserService;
import com.qf.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    public void checkUsername(String username) {
        if (username==null || username.trim().length()==0){
            log.info(" 【异步校验用户名】 用户名为空！");
            throw new SsmException(ExceptionEnum.CHECK_USERNAME_ERROR);
        }
        User user = new User();
        user.setUsername(username);
        int count = userMapper.selectCount(user);
        if (count!=0){
            log.info("【异步校验用户名】用户名已存在！！username = {}",username);
            throw new SsmException(11, "用户名已存在！！");
        }
    }

    @Transactional
    public void register(User user){
        String salt = UUID.randomUUID().toString();
        String newPassword = EncryptionUtil.encryption(user.getPassword(),salt);
        user.setPassword(newPassword);
        user.setSalt(salt);
        //user.setCreated(new Date());
        int count = userMapper.insertSelective(user);
        if (count!=1){
            log.info(" 【用户注册】 用户注册失败！！user = {}" , user);
            throw new SsmException(ExceptionEnum.USER_REGISTER_ERROR);
        }
    }

    public User login(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User newUser = userMapper.selectOne(user);
        if (newUser == null){
            log.info( " 【执行登陆】用户不存在！ username = {} " , username);
            throw new SsmException(ExceptionEnum.USER_EXISTENCE_ERROR);
        }
        if (!newUser.getPassword().equals(EncryptionUtil.encryption(password, newUser.getSalt()))){
            log.info( " 【执行登陆】密码错误！ password = {} " , password);
            throw new SsmException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        return newUser;
    }
}
