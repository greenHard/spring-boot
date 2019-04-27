package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.HelloController
 * @Description: 控制器
 * @create 2018/07/12 10:31
 */
@Controller
public class HelloController {

    @RequestMapping("/index")
    public String hello(){
        return "index";
    }
}
