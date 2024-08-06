package com.example.EconomyBoardGame.dto;

public class MiningResult {
    private boolean success;
    private int gold;
    private String message;

    public MiningResult(boolean success, int gold, String message) {
        this.success = success;
        this.gold = gold;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getGold() {
        return gold;
    }

    public String getMessage() {
        return message;
    }
}
