package com.example.EconomyBoardGame.repository;

import com.example.EconomyBoardGame.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByName(String name);
}