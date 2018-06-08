package com.example.SpringBootWeb.jsp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JspDemoController  {

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("message","hello jsp");
        return "index";
    }
}
