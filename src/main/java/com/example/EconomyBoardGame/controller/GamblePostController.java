package com.example.EconomyBoardGame.controller;

import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.service.GamblePostService;
import com.example.EconomyBoardGame.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GamblePostController {

    @Autowired
    private GamblePostService gamblePostService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/board/3")
    public String gamble(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);
        String result = gamblePostService.gamble(id, member);
        model.addAttribute("result", result);
        model.addAttribute("member", member);
        return "gambleResult";
    }
}
