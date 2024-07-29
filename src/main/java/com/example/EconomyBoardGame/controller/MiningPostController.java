package com.example.EconomyBoardGame.controller;

import com.example.EconomyBoardGame.dto.MiningResult;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.service.MemberService;
import com.example.EconomyBoardGame.service.MiningPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MiningPostController {

    @Autowired
    private MiningPostService miningPostService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/board/mining/{id}")
    public String mine(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);

        Post post = miningPostService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        MiningResult result = miningPostService.mine(member, post);

        if (result.isSuccess()) {
            model.addAttribute("message", "채굴 성공! " + result.getGold() + " 골드를 획득했습니다.");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "채굴 실패! 골드를 획득하지 못했습니다.");
            model.addAttribute("messageType", "failure");
        }

        model.addAttribute("member", member);
        return showMiningBoard(model);
    }

    @GetMapping("/board/1")
    public String showMiningBoard(Model model) {
        model.addAttribute("posts", miningPostService.getMiningBoard().getPosts());
        return "miningBoard";
    }
}