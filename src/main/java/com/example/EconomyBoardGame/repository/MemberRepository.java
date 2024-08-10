package com.example.EconomyBoardGame.repository;

import com.example.EconomyBoardGame.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}