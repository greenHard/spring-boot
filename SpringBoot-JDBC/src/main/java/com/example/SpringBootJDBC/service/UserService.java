package com.example.SpringBootJDBC.service;

import com.example.SpringBootJDBC.domain.User;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootJDBC.service.UserService
 * @Description: 用户接口
 * @create 2018/04/09 17:44
 */
public interface UserService {
    boolean addUser(User user);
}
