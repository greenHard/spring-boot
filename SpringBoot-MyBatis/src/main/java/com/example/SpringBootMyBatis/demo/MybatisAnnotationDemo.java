package com.example.SpringBootMyBatis.demo;

import com.example.SpringBootMyBatis.entity.User;
import com.example.SpringBootMyBatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootMyBatis.demo.MybatisAnnotationDemo
 * @Description: mybatis注解配置
 * @create 2018/04/12 16:20
 */
public class MybatisAnnotationDemo {
    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("mybatis/mybatis-config.xml");

        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");

        Reader reader = encodedResource.getReader();

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

        SqlSessionFactory sqlSessionFactory = builder.build(reader);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        User user = userMapper.selectUser(1);

        Optional.of(user).ifPresent(System.out::println);
    }
}
