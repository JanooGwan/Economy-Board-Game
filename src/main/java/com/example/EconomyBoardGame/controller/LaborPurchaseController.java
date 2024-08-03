package com.example.EconomyBoardGame.controller;

import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.service.LaborPurchaseService;
import com.example.EconomyBoardGame.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LaborPurchaseController {

    @Autowired
    private LaborPurchaseService laborPurchaseService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/board/4")
    public String purchaseLabor(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);
        String result = laborPurchaseService.purchaseLabor(id, member);
        model.addAttribute("result", result);
        model.addAttribute("messageType", result.startsWith("인력 구매에 성공했습니다.") ? "success" : "failure");
        model.addAttribute("posts", laborPurchaseService.getLaborPurchaseBoard().getPosts());
        return "laborPurchaseBoard";
    }
}
