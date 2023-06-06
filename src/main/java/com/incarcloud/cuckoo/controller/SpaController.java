package com.incarcloud.cuckoo.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class SpaController {
    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}
