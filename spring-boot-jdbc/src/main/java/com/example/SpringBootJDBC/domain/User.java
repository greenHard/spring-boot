package com.example.SpringBootJDBC.domain;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootJDBC.domain.User
 * @Description: 用户实体类
 * @create 2018/04/09 11:05
 */
public class User {
    private int id;

    private String name;

    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
