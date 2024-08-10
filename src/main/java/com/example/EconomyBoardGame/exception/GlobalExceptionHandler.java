package com.example.EconomyBoardGame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public String handleNicknameAlreadyExistsException(NicknameAlreadyExistsException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "register";
    }
}