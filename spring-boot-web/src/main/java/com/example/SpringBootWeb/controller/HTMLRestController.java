package com.example.SpringBootWeb.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
public class HTMLRestController {

    @GetMapping(path = {"/html/demo"})
    @PostMapping(path = {"/html/demo2"})
    public String html() {
        return "<html><body>Hello, World</body></html>";
    }

    @GetMapping("/html/demo/{message}")
    public String htmlPathVariable(@PathVariable() String message) {
        return "<html><body>Hello, " + message + "</body></html>";
    }

    @GetMapping("/html/demo/param")
    public String htmlParam(@RequestParam(name = "name") String param, @RequestParam(value = "age") Integer age, HttpServletRequest request) {
        String param2 = request.getParameter("param2");
        return "<html><body> Request Parameter1 value : " + param
                + " , parameter2 value :" + param2 +
                " , age value :" + age +
                " </body></html>";
    }

    @GetMapping("/html/demo/header")
    public String htmlHeader(@RequestHeader(name = "Accept") String acceptHeader) {
        return "<html><body>Request 'Accept' header value : " + acceptHeader + " </body></html>";
    }

    @GetMapping(path = "/html/demo/response/entity")
    public ResponseEntity<String> htmlResponseEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("MyHeader", Arrays.asList("MyHeaderValue"));
        ResponseEntity responseEntity = new ResponseEntity("<html><body>HTML ResponseEntity </body></html>", httpHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
