package com.example.SpringBootMyBatis.controller;

import com.example.SpringBootMyBatis.entity.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootMyBatis.controller.MybatisController
 * @Description: mybatis控制器
 * @create 2018/04/12 16:29
 */
@RestController
public class MybatisController {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @RequestMapping("user/{id}")
    public User user(@PathVariable int id){
        return sqlSessionTemplate.selectOne("com.example.SpringBootMyBatis.mapper.UserMapper.selectOneUser", id);
    }
}
