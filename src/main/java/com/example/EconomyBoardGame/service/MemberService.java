package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.exception.NicknameAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.repository.MemberRepository;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Member register(Member member) {
        if (memberRepository.existsByNickname(member.getNickname())) {
            throw new NicknameAlreadyExistsException("이미 존재하는 닉네임입니다.");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public void updateMember(Member member) {
        memberRepository.save(member);
    }
}