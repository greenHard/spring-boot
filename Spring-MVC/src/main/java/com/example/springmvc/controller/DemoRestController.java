package com.example.springmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.DemoRestController
 * {@link RestController}
 * {@link ResponseBody}
 * @Description: 演示，使用RestController,返回数据时不需要ResponseBody
 * @create 2018/07/12 11:15
 */
@RestController
@RequestMapping("/rest")
public class DemoRestController {

    /**
     * 返回数据的媒体类型为json
     * 直接返回对象,对象会在自动转换为json
     */
    @RequestMapping(value = "/getJson", produces = {"application/json;charset=UTF-8"})
    public DemoObj getJson(DemoObj obj) {
        return new DemoObj(obj.getName() + "dd");
    }

    /**
     * 返回数据的媒体类型为xml
     * 直接返回对象,对象会在自动转换为xml
     */
    @RequestMapping(value = "/getXml", produces = {"application/xml;charset=UTF-8"})
    public DemoObj getXml(DemoObj obj) {
        return new DemoObj(obj.getName() + "dd");
    }
}
