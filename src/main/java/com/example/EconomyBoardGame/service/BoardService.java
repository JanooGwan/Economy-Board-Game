package com.example.EconomyBoardGame.service;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Board findByName(String name) {
        return boardRepository.findByName(name);
    }

    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }
}