package com.demo.campingnavi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/authentication-fail")
    public String authenticationFail() {
        return "security/authenticationError";
    }

    @GetMapping("/authorization-fail")
    public String authorizationFail() {
        return "security/authorizationError";
    }
}
