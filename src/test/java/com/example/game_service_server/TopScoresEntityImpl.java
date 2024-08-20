package com.example.game_service_server;

import com.example.game_service_server.entity.TopScoresEntity;

public class TopScoresEntityImpl implements TopScoresEntity {
    private final int playerId;
    private final String playerName;
    private final int score;

    public TopScoresEntityImpl(int playerId, String playerName, int score) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int getScore() {
        return score;
    }
}

