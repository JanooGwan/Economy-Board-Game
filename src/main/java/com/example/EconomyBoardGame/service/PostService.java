package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class PostService {
    @Autowired
    private PostRepository miningPostRepository;

    @Autowired
    private MemberService memberService;

    private final Random random = new Random();

    public Optional<Post> findById(Long id) {
        return miningPostRepository.findById(id);
    }

    public void mine(Member member, Post post, String miningType) {
        int gold = 0;
        double successChance;
        int minePower = member.getMinepower();

        switch (miningType) {
            case "초급":
                successChance = 1.0;
                gold = random.nextInt(6) + 5;
                break;
            case "중급":
                successChance = 0.9;
                if (random.nextDouble() <= successChance) {
                    gold = random.nextInt(5) + 1 + minePower;
                }
                break;
            case "고급":
                successChance = 0.1;
                if (random.nextDouble() <= successChance) {
                    gold = minePower * 10;
                }
                break;
            case "특수":
                successChance = 0.00001;
                if (random.nextDouble() <= successChance) {
                    gold = minePower * 30000;
                }
                break;
        }

        member.setGold(member.getGold() + gold);
        memberService.updateMember(member);
    }
}
