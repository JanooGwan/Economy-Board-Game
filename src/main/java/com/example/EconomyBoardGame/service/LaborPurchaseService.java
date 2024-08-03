package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.repository.BoardRepository;
import com.example.EconomyBoardGame.repository.MemberRepository;
import com.example.EconomyBoardGame.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LaborPurchaseService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    public String purchaseLabor(Long postId, Member member) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return "존재하지 않는 구매 옵션입니다.";
        }

        Post post = optionalPost.get();
        String title = post.getTitle();
        int cost = 0;

        switch (title) {
            case "초급 인력":
                cost = 10000;
                break;
            case "중급 인력":
                cost = 30000;
                break;
            case "고급 인력":
                cost = 50000;
                break;
            default:
                return "존재하지 않는 구매 옵셥입니다.";
        }

        if (member.getGold() < cost) {
            return "인력 구매에 필요한 골드가 부족합니다.";
        }

        member.setGold(member.getGold() - cost);

        switch (title) {
            case "초급 인력":
                member.setManpowerLow(member.getManpowerLow() + 1);
                break;
            case "중급 인력":
                member.setManpowerMedium(member.getManpowerMedium() + 1);
                break;
            case "고급 인력":
                member.setManpowerHigh(member.getManpowerHigh() + 1);
                break;
        }

        memberRepository.save(member);
        return title + " 인력 구매를 성공했습니다.";
    }

    @Scheduled(fixedRate = 10000)
    public void generateGold() {
        Iterable<Member> members = memberRepository.findAll();
        for (Member member : members) {
            int goldEarned = (member.getManpowerLow() * 1) + (member.getManpowerMedium() * 3) + (member.getManpowerHigh() * 5);
            member.setGold(member.getGold() + goldEarned);
            memberRepository.save(member);
        }
    }

    public Board getLaborPurchaseBoard() {
        return boardRepository.findByName("인력구매게시판");
    }
}
