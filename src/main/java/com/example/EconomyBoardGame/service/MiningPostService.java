package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.dto.MiningResult;
import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.repository.BoardRepository;
import com.example.EconomyBoardGame.repository.MemberRepository;
import com.example.EconomyBoardGame.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Random;

@Service
public class MiningPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private final Random random = new Random();

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public MiningResult mine(Member member, Post post, HttpSession session, String inputCaptcha) {
        if (member.getClickCount() >= 100) {
            String captcha = (String) session.getAttribute("captcha");
            if (captcha == null) {
                captcha = generateCaptcha();
                session.setAttribute("captcha", captcha);
            }
            if (inputCaptcha != null) {
                if (!verifyCaptcha(session, inputCaptcha)) {
                    return new MiningResult(false, 0, "CAPTCHA verification required");
                } else {
                    member.setClickCount(0);
                }
            } else {
                return new MiningResult(false, 0, "CAPTCHA verification required");
            }
        }

        int gold = 0;
        boolean isSuccess = false;
        double successChance;
        int minePower = member.getMinepower();
        String miningType = post.getTitle().toLowerCase();

        switch (miningType) {
            case "초급 채굴":
                successChance = 1.0;
                gold = random.nextInt(3) + 1;
                isSuccess = true;
                break;
            case "중급 채굴":
                successChance = 0.9;
                if (random.nextDouble() <= successChance) {
                    gold = random.nextInt(5) + 1 + minePower;
                    isSuccess = true;
                }
                break;
            case "고급 채굴":
                successChance = 0.1;
                if (random.nextDouble() <= successChance) {
                    gold = minePower * 10;
                    isSuccess = true;
                }
                break;
            case "특수 채굴":
                successChance = 0.00001;
                if (random.nextDouble() <= successChance) {
                    gold = minePower * 30000;
                    isSuccess = true;
                }
                break;
        }

        if (isSuccess) {
            member.setGold(member.getGold() + gold);
            memberService.updateMember(member);
        }

        return new MiningResult(isSuccess, gold, null);
    }

    public String generateCaptcha() {
        Random random = new Random();
        int captcha = random.nextInt(9000) + 1000; // 1000부터 9999 사이의 네 자리 숫자 생성
        return String.valueOf(captcha);
    }

    public boolean verifyCaptcha(HttpSession session, String inputCaptcha) {
        String captcha = (String) session.getAttribute("captcha");
        return captcha != null && captcha.equals(inputCaptcha);
    }

    public Board getMiningBoard() {
        return boardRepository.findByName("채굴게시판");
    }
}
