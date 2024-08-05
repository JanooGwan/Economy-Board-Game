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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/board/mining")
public class MiningPostController {

    @Autowired
    private MiningPostService miningPostService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/{id}")
    public String mine(@PathVariable Long id, Model model, HttpSession session, @RequestParam(required = false) String inputCaptcha) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);
        Optional<Post> postOpt = miningPostService.findById(id);

        if (postOpt.isPresent()) {
            Post post = postOpt.get();

            if (member.getClickCount() >= 100) {
                if (inputCaptcha != null && miningPostService.verifyCaptcha(session, inputCaptcha)) {
                    member.setClickCount(0);
                } else {
                    Integer captcha = (Integer) session.getAttribute("captcha");
                    if (captcha == null) {
                        captcha = miningPostService.generateCaptcha();
                        session.setAttribute("captcha", captcha);
                    }
                    model.addAttribute("captcha", captcha);
                    return "captcha";
                }
            }

            MiningResult result = miningPostService.mine(member, post, session, inputCaptcha);
            model.addAttribute("result", result);
            return "miningResult";
        }

        return "redirect:/board";
    }

    @PostMapping("/verifyCaptcha")
    public String verifyCaptcha(@RequestParam String inputCaptcha, HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);

        if (miningPostService.verifyCaptcha(session, inputCaptcha)) {
            member.setClickCount(0);
            memberService.updateMember(member);
            return "redirect:/board/mining";
        }

        Integer generatedCaptcha = (Integer) session.getAttribute("captcha");
        model.addAttribute("error", "매크로 검증에 실패했습니다.");
        model.addAttribute("captcha", generatedCaptcha);
        return "captcha";
    }
}