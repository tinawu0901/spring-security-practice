package com.tinawu.springSecuritybase.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class StringController {


    @GetMapping("index")
    public String index() {
        return "forward:index.html";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }


}
