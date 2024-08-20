package com.example.game_service_server.non_entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileData {

    private int playerId;
    private String playerName;
    private int score;

    @Override
    public String toString() {
        return "FileData{" + "playerId='" + playerId + '\'' + ", playerName='" + playerName + '\'' + ", score='" + score
                + '\'' + '}';
    }
}
