package com.example.EconomyBoardGame.controller;

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

    @GetMapping("/board/{id}")
    public String mine(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);

        Post post = miningPostService.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        miningPostService.mine(member, post);

        model.addAttribute("member", member);
        model.addAttribute("message", "채굴 성공!");

        return "redirect:/account";
    }

    @GetMapping("/board/miningboard")
    public String showMiningBoard(Model model) {
        model.addAttribute("posts", miningPostService.getMiningBoard().getPosts());
        return "miningBoard";
    }
}