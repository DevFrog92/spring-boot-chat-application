package com.example.chat.domain.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {
    @GetMapping
    public String home() {
        return "redirect:/chat/room";
    }
}
