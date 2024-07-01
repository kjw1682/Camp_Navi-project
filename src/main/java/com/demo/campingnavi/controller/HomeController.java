package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class HomeController {
    @RequestMapping("/")
    public String goindex() {
        return "member/loginPage";
    }

    @RequestMapping("/main")
    public String gomain(Model model) {
        return "mainPage";
    }

    @ResponseBody
    @PostMapping("/get_nickname")
    public Map<String, Object> getNickname(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String nickname = null;
        if (session.getAttribute("loginUser") != null) {
            nickname = ((Member)session.getAttribute("loginUser")).getNickname();
        }
        result.put("nickname", nickname);
        return result;
    }

    @RequestMapping("/loading")
    public String loading() {
        return "loadingPage";
    }

    @GetMapping("/search/loading/{cseq}")
    public String loading_search(@PathVariable("cseq") int cseq, Model model) {
        model.addAttribute("cseq", cseq);
        return "search/loadingPage_search";
    }
}
