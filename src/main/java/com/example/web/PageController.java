package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/wiki/write")
    public String writePage() {
        return "write";
    }

    @GetMapping("/wiki/{id}")
    public String detailPage(@PathVariable Long id) {
        return "detail";
    }
}
