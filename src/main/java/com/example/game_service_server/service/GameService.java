package com.example.game_service_server.service;

import com.example.game_service_server.entity.TopScoresEntity;
import com.example.game_service_server.non_entity.request.PlayerScore;
import com.example.game_service_server.non_entity.request.PlayerDetailsRequest;
import com.example.game_service_server.non_entity.response.PlayerDetailsResponse;
import com.example.game_service_server.non_entity.response.ScoreAdditionResponse;

import java.util.List;

public interface GameService {

    List<PlayerDetailsResponse> addPlayers(String requestId, List<PlayerDetailsRequest> playerDetailsRequestList);

    ScoreAdditionResponse addScores(List<PlayerScore> playerScoreList, String requestId);

    List<TopScoresEntity> findTopNPlayersByScores(int numberOfRecords, boolean orderByDesc, String requestId);
}
