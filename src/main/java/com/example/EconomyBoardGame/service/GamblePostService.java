package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.repository.BoardRepository;
import com.example.EconomyBoardGame.repository.MemberRepository;
import com.example.EconomyBoardGame.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GamblePostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    public String gamble(Long postId, Member member) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return "존재하지 않는 도박 옵션입니다.";
        }

        Post post = optionalPost.get();
        String title = post.getTitle();
        int goldCost = 0;
        double successRate = 0.5;
        double rewardMultiplier = 2;

        switch (title) {
            case "초급 도박":
                goldCost = 100;
                break;
            case "중급 도박":
                goldCost = 500;
                break;
            case "고급 도박":
                goldCost = 1000;
                break;
            case "특수 도박":
                goldCost = 50000;
                successRate = 0.1;
                rewardMultiplier = 20;
                break;
            default:
                return "존재하지 않는 도박 옵션입니다.";
        }

        if (member.getGold() < goldCost) {
            return "골드가 부족합니다.";
        }

        member.setGold(member.getGold() - goldCost);

        if (Math.random() < successRate) {
            int reward = (int) (goldCost * rewardMultiplier);
            member.setGold(member.getGold() + reward);
            memberRepository.save(member);
            return "도박에 성공했습니다! " + reward + " 골드를 벌었습니다.";
        } else {
            memberRepository.save(member);
            return "도박에 실패했습니다... " + goldCost + " 골드를 잃었습니다.";
        }
    }

    public Board getGambleBoard() {
        return boardRepository.findByName("도박게시판");
    }
}