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

    public MiningResult mine(Member member, Post post) {
        int gold = 0;
        boolean isSuccess = false;
        double successChance;
        int minePower = member.getMinepower();
        String miningType = post.getTitle().toLowerCase();

        if (member.getClickCount() >= 100) {
            return new MiningResult(false, 0);
        }

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

        member.setClickCount(member.getClickCount() + 1);
        memberRepository.save(member);

        return new MiningResult(isSuccess, gold);
    }

    public Board getMiningBoard() {
        return boardRepository.findByName("채굴게시판");
    }

    public int generateCaptcha() {
        return random.nextInt(9000) + 1000;
    }

    public boolean verifyCaptcha(HttpSession session, String inputCaptcha) {
        Integer generatedCaptcha = (Integer) session.getAttribute("captcha");
        if (generatedCaptcha != null && generatedCaptcha.toString().equals(inputCaptcha)) {
            session.removeAttribute("captcha");
            return true;
        }
        return false;
    }
}

