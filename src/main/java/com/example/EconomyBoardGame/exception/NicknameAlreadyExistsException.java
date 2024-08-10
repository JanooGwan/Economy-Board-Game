package com.example.EconomyBoardGame.exception;

public class NicknameAlreadyExistsException extends RuntimeException {
    public NicknameAlreadyExistsException(String message) {
        super(message);
    }
}
