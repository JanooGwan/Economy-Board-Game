package com.example.EconomyBoardGame.repository;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
}