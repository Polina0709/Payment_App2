package com.flowbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping(value = {"/", "/client/**", "/admin/**"})
    public String index() {
        return "forward:/index.html";
    }
}
