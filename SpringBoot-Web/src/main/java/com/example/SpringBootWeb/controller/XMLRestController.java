package com.example.SpringBootWeb.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * XML Rest 控制器
 */
@RestController
public class XMLRestController {

    @GetMapping(path = "/xml/person",
            produces = MediaType.APPLICATION_XML_VALUE)
    public Person getPerson() {
        Person person = new Person("zhangsan", "shanghaipudong");
        return person;
    }
}
