package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.exception.NicknameAlreadyExistsException;
import com.example.EconomyBoardGame.exception.NullContentException;
import com.example.EconomyBoardGame.exception.TooLongInformationException;
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

        if(member.getNickname().isEmpty()) {
            throw new NullContentException("닉네임에 대한 정보가 없습니다.");
        }

        else if(member.getPassword().isEmpty()) {
            throw new NullContentException("비밀번호에 대한 정보가 없습니다.");
        }

        else if(member.getIntroduction().isEmpty()) {
            throw new NullContentException("자기소개에 대한 정보가 없습니다.");
        }

        else if(member.getNickname().length() > 32) {
            throw new TooLongInformationException("닉네임은 최대 32자까지 가능합니다.");
        }

        else if(member.getPassword().length() > 32) {
            throw new TooLongInformationException("비밀번호는 최대 32자까지 가능합니다.");
        }

        else if(member.getIntroduction().length() > 200) {
            throw new TooLongInformationException("자기소개는 최대 200자까지 가능합니다.");
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