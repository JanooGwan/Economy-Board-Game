package com.example.EconomyBoardGame.dto;

public class MiningResult {
    private boolean success;
    private int gold;


    public MiningResult(boolean success, int gold) {
        this.success = success;
        this.gold = gold;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}