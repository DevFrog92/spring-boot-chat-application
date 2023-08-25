package com.example.chat.global.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {
    @GetMapping
    public String home() {
        return "redirect:/chat/room";
    }
}
