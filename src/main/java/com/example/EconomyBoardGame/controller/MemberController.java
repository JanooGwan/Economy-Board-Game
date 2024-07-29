package com.example.EconomyBoardGame.controller;

import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String registerMember(@Valid @ModelAttribute("member") Member member, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("오류 발생...");
            return "register";
        }
        memberService.register(member);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String showLogout() {
        return "logout";
    }

    @GetMapping("/account")
    public String showAccountPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);
        model.addAttribute("member", member);
        return "account";
    }

    @PostMapping("/upgradeMiningPower")
    public String upgradeMiningPower(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);

        int currentMiningPower = member.getMinepower();
        int upgradeCost = (currentMiningPower + 1) * 100;

        if (member.getGold() >= upgradeCost) {
            member.setGold(member.getGold() - upgradeCost);
            member.setMinepower(currentMiningPower + 1);
            memberService.updateMember(member);
            model.addAttribute("message", "채굴력이 업그레이드되었습니다!");
        } else {
            model.addAttribute("message", "골드가 부족합니다.");
        }

        model.addAttribute("member", member);
        return "account";
    }
}