package com.example.SpringBootJDBC.service;

import com.example.SpringBootJDBC.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootJDBC.service.UserServiceImpl
 * @Description: 用户接口实现类
 * @create 2018/04/09 17:45
 */
@EnableTransactionManagement
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public boolean addUser(User user) {
        Boolean result = jdbcTemplate.execute("INSERT INTO user (name,age) VALUES (?,?)", (PreparedStatement ps) -> {
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            return ps.executeUpdate() > 0;
        });
        return result;
    }
}
