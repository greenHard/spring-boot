package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.DemoAnnoController
 * @Description: 注解演示控制器
 * @create 2018/07/12 10:51
 */
@Controller
@RequestMapping("/anno")
public class DemoAnnoController {

    /**
     * 此方法未标明路径,因此使用类级别的路径/anno;
     * produces可定制返回的媒体类型和字符集,若需返回json对象,
     * 则设置produces = "application/json;charset=UTF-8"
     * 演示可接受HttpServletRequest作为参数,当然也可以接受
     * HttpServletResponse作为参数。此处的ResponseBody用在返回值前面
     */
    @RequestMapping(produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String index(HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access";
    }

    /**
     * 演示接受路径参数,并在方法参数前结合@PathVariable 使用,
     * 访问路径为/anno/pathvar/{str}
     */
    @RequestMapping(value = "/pathvar/{str}", produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String demoPathVar(@PathVariable String str, HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access,str: " + str;
    }


    /**
     * 演示常规的request参数获取,访问路径为/anno/requestParam?id=1,
     */
    @RequestMapping(value = "/requestParam", produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String passRequestParam(Long id, HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access,id: " + id;
    }

    /**
     * 演示接受参数到对象,访问路径为/anno/obj?name=xx
     * {@link ResponseBody }也可以用在方法上
     */
    @RequestMapping(value = "/obj", produces = "application/json;charset=UTF-8") // 7
    @ResponseBody
    public String passObj(DemoObj obj, HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access, obj name : " + obj.getName();
    }

    /**
     * 演示不同路径到相同的方法。
     * 访问路径为/anno/name1 或 /anno/name2
     */
    @RequestMapping(value = {"/name1", "/name2"}, produces = "text/plain;charset=UTF-8") //9
    public @ResponseBody
    String remove(HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access";
    }


}
