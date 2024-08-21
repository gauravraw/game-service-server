package com.example.game_service_server.service.impl;

import com.example.game_service_server.entity.PlayerDetailsEntity;
import com.example.game_service_server.entity.ScoreDetailsEntity;
import com.example.game_service_server.entity.TopScoresEntity;
import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.enums.PlayerStatus;
import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.request.PlayerScore;
import com.example.game_service_server.non_entity.request.PlayerDetailsRequest;
import com.example.game_service_server.non_entity.response.PlayerDetailsResponse;
import com.example.game_service_server.non_entity.response.ScoreAdditionResponse;
import com.example.game_service_server.repository.PlayerDetailsRepository;
import com.example.game_service_server.repository.ScoreDetailsRepository;
import com.example.game_service_server.service.GameService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final PlayerDetailsRepository playerDetailsRepository;

    private final ScoreDetailsRepository scoreDetailsRepository;

    @Override
    @Transactional
    public List<PlayerDetailsResponse> addPlayers(String requestId,
            List<PlayerDetailsRequest> playerDetailsRequestList) {
        log.info("Received request for adding player details with requestId :: {}", requestId);

        try {
            List<PlayerDetailsEntity> playerDetailsEntityList = mapPlayersDetailsListToEntity(playerDetailsRequestList);
            if (playerDetailsEntityList.isEmpty()) {
                log.info("Empty request for saving player details in db");
                return Collections.emptyList();
            }
            log.info("Saving {} playerDetails into database", playerDetailsEntityList.size());
            List<PlayerDetailsEntity> savedPlayerEntitiesList = playerDetailsRepository
                    .saveAll(playerDetailsEntityList);
            log.info("Saved {} playerDetails into database", savedPlayerEntitiesList.size());
            // TODO :: Currently player id is returned as auto generated serial number. But can be replaced with custom
            // logic
            return mapPlayerDetailsResponse(savedPlayerEntitiesList);
        }
        // TODO :: Catch SQL exception separately
        catch (Exception exception) {
            log.error("Error occurred while saving records in db ", exception);
            throw new GameServiceException(ErrorCode.UNABLE_TO_SAVE_RECORDS);
        }
    }

    @Override
    @Transactional
    public ScoreAdditionResponse addScores(List<PlayerScore> playerScoreList, String requestId) {

        log.info("Received request for adding scores for players in database with requestId :: {}", requestId);

        try {

            // TODO :: Can be improved via foreign key logic.
            Set<Integer> playerIdSet = playerScoreList.stream().map(PlayerScore::getPlayerId)
                    .collect(Collectors.toSet());
            Set<Integer> nonExistingPlayerIdSet = getNonExistingPlayerIdSet(playerIdSet);

            if (nonExistingPlayerIdSet.size() == playerIdSet.size()) {
                log.error("None of the players exist in database");
                throw new GameServiceException(ErrorCode.UNABLE_TO_SAVE_SCORE);
            }

            // add scores logic
            Map<Integer, Integer> playerIdScoreMap = getExistingPlayerScoreMap(playerScoreList, nonExistingPlayerIdSet);
            List<ScoreDetailsEntity> scoreDetailsEntityList = new ArrayList<>();
            playerIdScoreMap.forEach((playerId, score) -> {
                // check if score details repository already has their records
                Optional<ScoreDetailsEntity> optionalScoreDetailsEntity = scoreDetailsRepository
                        .findByPlayerId(playerId);
                if (optionalScoreDetailsEntity.isEmpty()) {
                    scoreDetailsEntityList.add(ScoreDetailsEntity.builder().playerId(playerId).score(score).build());
                } else {
                    ScoreDetailsEntity scoreDetailsEntity = optionalScoreDetailsEntity.get();
                    scoreDetailsEntity.setScore(scoreDetailsEntity.getScore() + score);
                    scoreDetailsEntityList.add(scoreDetailsEntity);
                }
            });
            log.info("Saving {} records in score table", scoreDetailsEntityList.size());
            List<ScoreDetailsEntity> updatedScoreEntityList = scoreDetailsRepository.saveAll(scoreDetailsEntityList);
            log.info("Successfully saved {} records in database", updatedScoreEntityList.size());

            List<Integer> processedPlayerIds = updatedScoreEntityList.stream().map(ScoreDetailsEntity::getPlayerId)
                    .collect(Collectors.toList());
            return ScoreAdditionResponse.builder().processedPlayerIds(processedPlayerIds).build();
        } catch (GameServiceException exception) {
            log.error("Error occurred while saving player score details", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while saving player score details", exception);
            throw new GameServiceException(ErrorCode.UNABLE_TO_SAVE_RECORDS);
        }
    }

    @Override
    public List<TopScoresEntity> findTopNPlayersByScores(int numberOfRecords, boolean orderByDesc, String requestId) {

        log.info("Received request to fetch top {} players from db with requestId :: {}", numberOfRecords, requestId);

        if (orderByDesc) {
            return scoreDetailsRepository.findTopNPlayersByScoreDesc(numberOfRecords);
        }
        return scoreDetailsRepository.findTopNPlayersByScoreAsc(numberOfRecords);
    }

    private static Map<Integer, Integer> getExistingPlayerScoreMap(List<PlayerScore> playerScoreList,
            Set<Integer> nonExistingPlayerIdSet) {
        Map<Integer, Integer> playerIdScoreMap = new HashMap<>();
        playerScoreList.stream().filter(fileData -> !nonExistingPlayerIdSet.contains(fileData.getPlayerId()))
                .forEach(fileData -> {
                    playerIdScoreMap.put(fileData.getPlayerId(),
                            playerIdScoreMap.getOrDefault(fileData.getPlayerId(), 0) + fileData.getScore());
                });
        return playerIdScoreMap;
    }

    private Set<Integer> getNonExistingPlayerIdSet(Set<Integer> playerIdSet) {
        Set<Integer> nonExistingPlayerIdSet = new HashSet<>();
        playerIdSet.forEach(playerId -> {
            boolean playerExists = playerDetailsRepository.existsById(playerId);
            if (!playerExists) {
                nonExistingPlayerIdSet.add(playerId);
            }
        });
        return nonExistingPlayerIdSet;
    }

    private List<PlayerDetailsResponse> mapPlayerDetailsResponse(List<PlayerDetailsEntity> playerDetailsEntityList) {
        if (playerDetailsEntityList.isEmpty()) {
            return Collections.emptyList();
        }
        return playerDetailsEntityList
                .stream().map(playerDetailsEntity -> PlayerDetailsResponse.builder()
                        .playerId(playerDetailsEntity.getId()).playerName(playerDetailsEntity.getName()).build())
                .collect(Collectors.toList());
    }

    private List<PlayerDetailsEntity> mapPlayersDetailsListToEntity(
            List<PlayerDetailsRequest> playerDetailsRequestList) {

        if (playerDetailsRequestList.isEmpty()) {
            return Collections.emptyList();
        }

        return playerDetailsRequestList.stream()
                .map(playerDetailsRequest -> PlayerDetailsEntity.builder().name(playerDetailsRequest.getName())
                        .team(playerDetailsRequest.getTeam())
                        .status(PlayerStatus.valueOf(playerDetailsRequest.getPlayerStatus())).build())
                .collect(Collectors.toList());
    }
}
