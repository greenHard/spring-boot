package com.example.SpringBootMyBatis.demo;

import com.example.SpringBootMyBatis.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootMyBatis.demo.MybatisXmlConfigurationDemo
 * @Description: Mybatis的Xml配置方式
 * @create 2018/04/12 11:19
 */
public class MybatisXmlConfigurationDemo {
    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("mybatis/mybatis-config.xml");

        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");

        Reader reader = encodedResource.getReader();

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

        SqlSessionFactory sqlSessionFactory = builder.build(reader);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = sqlSession.selectOne("com.example.SpringBootMyBatis.mapper.UserMapper.selectOneUser",1);

        Optional.of(user).ifPresent(System.out::println);
    }
}
