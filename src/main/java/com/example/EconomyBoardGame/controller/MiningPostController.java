package com.example.EconomyBoardGame.controller;

import com.example.EconomyBoardGame.dto.MiningResult;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.service.MemberService;
import com.example.EconomyBoardGame.service.MiningPostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MiningPostController {

    @Autowired
    private MiningPostService miningPostService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/board/mining/{id}")
    public String mine(@PathVariable Long id, Model model, HttpSession session, @RequestParam(required = false) String inputCaptcha) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);

        Post post = miningPostService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        if (member.getClickCount() >= 100) {
            Integer captcha = (Integer) session.getAttribute("captcha");
            if (captcha == null) {
                captcha = miningPostService.generateCaptcha();
                session.setAttribute("captcha", captcha);
            }
            if (inputCaptcha != null) {
                if (miningPostService.verifyCaptcha(session, inputCaptcha)) {
                    member.setClickCount(0); // 캡차 성공 시 클릭 카운트 초기화
                } else {
                    model.addAttribute("error", "CAPTCHA verification failed");
                    model.addAttribute("captcha", captcha);
                    return showMiningBoard(model, member);
                }
            } else {
                model.addAttribute("captcha", captcha);
                return showMiningBoard(model, member);
            }
        }

        MiningResult result = miningPostService.mine(member, post);

        if (result.isSuccess()) {
            model.addAttribute("message", "채굴 성공! " + result.getGold() + " 골드를 획득했습니다.");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "채굴 실패! 골드를 획득하지 못했습니다.");
            model.addAttribute("messageType", "failure");
        }

        model.addAttribute("member", member);
        return showMiningBoard(model, member);
    }

    @PostMapping("/board/mining/verifyCaptcha")
    public String verifyCaptcha(@RequestParam String inputCaptcha, HttpSession session, Model model) {
        Integer generatedCaptcha = (Integer) session.getAttribute("captcha");
        if (generatedCaptcha != null && generatedCaptcha.toString().equals(inputCaptcha)) {
            session.removeAttribute("captcha");
            return "redirect:/board/mining";
        }

        model.addAttribute("error", "CAPTCHA verification failed");
        model.addAttribute("captcha", generatedCaptcha);
        return "miningBoard";
    }

    @GetMapping("/board/1")
    public String showMiningBoard(Model model) {
        model.addAttribute("posts", miningPostService.getMiningBoard().getPosts());
        return "miningBoard";
    }

    public String showMiningBoard(Model model, Member member) {
        model.addAttribute("posts", miningPostService.getMiningBoard().getPosts());
        model.addAttribute("member", member);
        return "miningBoard";
    }
}