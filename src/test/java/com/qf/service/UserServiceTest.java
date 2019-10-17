package com.qf.service;

import com.qf.SpringTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends SpringTests {

    @Autowired
    private UserService userService;

    @Test
    public void checkUsername() {
        userService.checkUsername("admin123");
    }



}