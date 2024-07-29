package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.repository.BoardRepository;
import com.example.EconomyBoardGame.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class MiningPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardRepository boardRepository;

    private final Random random = new Random();

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public boolean mine(Member member, Post post) {
        int gold = 0;
        boolean isSuccess = false;
        double successChance;
        int minePower = member.getMinepower();
        String miningType = post.getTitle().toLowerCase();

        switch (miningType) {
            case "초급 채굴":
                successChance = 1.0;
                gold = random.nextInt(6) + 5;
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

        return isSuccess;
    }

    public Board getMiningBoard() {
        return boardRepository.findByName("채굴게시판");
    }
}