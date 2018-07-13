package com.example.springmvc.controller;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.DemoObj
 * @Description: demoObj
 * @create 2018/07/12 10:59
 */
@Data
public class DemoObj {

    public DemoObj(String name) {
        this.name = name;
    }

    private String name;

}
