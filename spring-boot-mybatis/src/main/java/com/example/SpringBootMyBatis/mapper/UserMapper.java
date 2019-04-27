package com.example.SpringBootMyBatis.mapper;

import com.example.SpringBootMyBatis.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Results(value = {
            @Result(column = "id",property = "id",id=true),
            @Result(column = "name",property = "name"),
            @Result(column = "age",property = "age")
    })
    @Select("SELECT * FROM user where id = #{id}")
    User selectUser(int id);
}
