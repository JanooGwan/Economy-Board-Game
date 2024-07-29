package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> findByBoard(Board board) {
        return postRepository.findByBoard(board);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void save(Post post) {
        postRepository.save(post);
    }
}