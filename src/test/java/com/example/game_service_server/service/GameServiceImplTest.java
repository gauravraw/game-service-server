package com.example.game_service_server.service;


import com.example.game_service_server.TopScoresEntityImpl;
import com.example.game_service_server.entity.PlayerDetailsEntity;
import com.example.game_service_server.entity.ScoreDetailsEntity;
import com.example.game_service_server.entity.TopScoresEntity;
import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.enums.PlayerStatus;
import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.request.FileData;
import com.example.game_service_server.non_entity.request.PlayerDetailsRequest;
import com.example.game_service_server.non_entity.response.PlayerDetailsResponse;
import com.example.game_service_server.repository.PlayerDetailsRepository;
import com.example.game_service_server.repository.ScoreDetailsRepository;
import com.example.game_service_server.service.impl.GameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private PlayerDetailsRepository playerDetailsRepository;

    @Mock
    private ScoreDetailsRepository scoreDetailsRepository;

    @Test
    public void setup(){
        log.info("test setup");
    }

    @Test
    public void addPlayerTestSuccess(){
        String requestId = "request-123";
        List<PlayerDetailsRequest> requests = Arrays.asList(
                new PlayerDetailsRequest("Alice", "TeamA", "ACTIVE"),
                new PlayerDetailsRequest("Bob", "TeamB", "INACTIVE")
        );
        List<PlayerDetailsEntity> entities = Arrays.asList(
                new PlayerDetailsEntity(1, "Alice", "TeamA", PlayerStatus.ACTIVE),
                new PlayerDetailsEntity(2, "Bob", "TeamB", PlayerStatus.INACTIVE)
        );

        when(playerDetailsRepository.saveAll(any())).thenReturn(entities);

        List<PlayerDetailsResponse> responses = gameService.addPlayers(requestId, requests);

        assertEquals(2, responses.size());
        assertEquals("Alice", responses.get(0).getPlayerName());
        assertEquals("Bob", responses.get(1).getPlayerName());

    }

    @Test
    public void addPlayerTestPartialSuccess(){
        String requestId = "request-123";
        List<PlayerDetailsRequest> requests = Arrays.asList(
                new PlayerDetailsRequest("Alice", "TeamA", "ACTIVE"),
                new PlayerDetailsRequest("Bob", "TeamB", "INACTIVE")
        );
        List<PlayerDetailsEntity> entities = Arrays.asList(
                new PlayerDetailsEntity(1, "Alice", "TeamA", PlayerStatus.ACTIVE),
                new PlayerDetailsEntity(2, "Bob", "TeamB", PlayerStatus.INACTIVE)
        );

        when(playerDetailsRepository.saveAll(any())).thenReturn(Collections.emptyList());

        List<PlayerDetailsResponse> responses = gameService.addPlayers(requestId, requests);

        assertEquals(0, responses.size());

    }

    @Test
    public void addPlayerEmptyListTestSuccess(){
        String requestId = "request-123";
        List<PlayerDetailsRequest> requests = Collections.emptyList();

        List<PlayerDetailsResponse> responses = gameService.addPlayers(requestId, requests);

        assertEquals(0, responses.size());
    }
    @Test
    void testAddPlayers_EmptyName() {
        String requestId = "request-123";
        List<PlayerDetailsRequest> requests = Collections.singletonList(
                new PlayerDetailsRequest("", "TeamA", "ACTIVE")
        );

        GameServiceException thrown = assertThrows(GameServiceException.class, () -> {
            gameService.addPlayers(requestId, requests);
        });
        assertEquals(ErrorCode.EMPTY_NAME.getCode(), thrown.getCode());
        assertEquals(ErrorCode.EMPTY_NAME.getErrorMsg() , thrown.getErrorMsg());
    }

    @Test
    void testAddPlayer_UnableToAddException(){
        String requestId = "request-123";
        List<PlayerDetailsRequest> requests = Collections.singletonList(
                new PlayerDetailsRequest("abc", "TeamA", "ACTIVE")
        );

        when(playerDetailsRepository.saveAll(any())).thenThrow(RuntimeException.class);
        GameServiceException thrown = assertThrows(GameServiceException.class, () -> {
            gameService.addPlayers(requestId, requests);
        });
        assertEquals(ErrorCode.UNABLE_TO_SAVE_RECORDS.getCode(), thrown.getCode());
        assertEquals(ErrorCode.UNABLE_TO_SAVE_RECORDS.getErrorMsg() , thrown.getErrorMsg());
    }

    @Test
    void testAddScores_Success() {
        String requestId = "request-123";
        List<FileData> fileDataList = Arrays.asList(
                new FileData(1,"Test-1" , 100),
                new FileData(2, "Test-2" , 200)
        );
        Set<Integer> existingPlayerIds = new HashSet<>(Arrays.asList(1, 2));
        Map<Integer, Integer> playerIdScoreMap = Map.of(1, 100, 2, 200);
        List<ScoreDetailsEntity> scoreEntities = Arrays.asList(
                new ScoreDetailsEntity(1, 1 ,  50),
                new ScoreDetailsEntity(2, 2 , 50)
        );

        when(playerDetailsRepository.existsById(any())).thenReturn(true);
        when(scoreDetailsRepository.findByPlayerId(1)).thenReturn(Optional.of(scoreEntities.get(0)));
        when(scoreDetailsRepository.findByPlayerId(2)).thenReturn(Optional.empty());
        when(scoreDetailsRepository.saveAll(any())).thenReturn(scoreEntities);

        String result = gameService.addScores(fileDataList, requestId);

        assertTrue(result.contains("Successfully updated scores for playerId ->  1,2,"));
    }

    @Test
    void testAddScores_NoExistingPlayer() {
        String requestId = "request-123";
        List<FileData> fileDataList = Collections.singletonList(
                new FileData(1, "test", 100)
        );
        Set<Integer> nonExistingPlayerIds = new HashSet<>(Collections.singletonList(1));

        when(playerDetailsRepository.existsById(anyInt())).thenReturn(false);

        GameServiceException thrown = assertThrows(GameServiceException.class, () -> {
            gameService.addScores(fileDataList, requestId);
        });
        assertEquals(ErrorCode.UNABLE_TO_SAVE_SCORE.getCode(), thrown.getCode());
        assertEquals(ErrorCode.UNABLE_TO_SAVE_SCORE.getErrorMsg(), thrown.getErrorMsg());
    }

    @Test
    void testNonExistingPlayerTest(){
        String requestId = "request-123";
        List<FileData> fileDataList = List.of(
                new FileData(1, "test", 100),
                new FileData(2, "test", 100)
        );
        Set<Integer> nonExistingPlayerIds = new HashSet<>(Collections.singletonList(1));

        List<ScoreDetailsEntity> scoreEntities = List.of(
                new ScoreDetailsEntity(1, 1, 50)
        );
        when(playerDetailsRepository.existsById(2)).thenReturn(false);
        when(playerDetailsRepository.existsById(1)).thenReturn(true);


        when(scoreDetailsRepository.saveAll(any())).thenReturn(scoreEntities);

        String message = gameService.addScores(fileDataList, requestId);

        assertEquals(message , "Successfully updated scores for playerId ->  1,. Unable to update score for following playerId -> 2,as these players doesn't exist");
    }

    @Test
    void testAddScores_UnableToAddScoreTest() {
        // Arrange
        String requestId = "request-123";
        List<FileData> fileDataList = Collections.singletonList(
                new FileData(1, "test", 100)
        );
        Set<Integer> nonExistingPlayerIds = new HashSet<>(Collections.singletonList(1));

        when(playerDetailsRepository.existsById(anyInt())).thenThrow(RuntimeException.class);

        // Act & Assert
        GameServiceException thrown = assertThrows(GameServiceException.class, () -> {
            gameService.addScores(fileDataList, requestId);
        });
        assertEquals(ErrorCode.UNABLE_TO_SAVE_RECORDS.getCode(), thrown.getCode());
        assertEquals(ErrorCode.UNABLE_TO_SAVE_RECORDS.getErrorMsg(), thrown.getErrorMsg());
    }

    @Test
    void testFindTopNPlayersByScoresDesc() {
        // Arrange
        int numberOfRecords = 5;
        List<TopScoresEntity> topScores = Arrays.asList(
                new TopScoresEntityImpl(1, "test-1" , 500),
                new TopScoresEntityImpl(2, "test-2" , 400)
        );
        when(scoreDetailsRepository.findTopNPlayersByScoreDesc(numberOfRecords)).thenReturn(topScores);

        // Act
        List<TopScoresEntity> result = gameService.findTopNPlayersByScores(numberOfRecords, true, "request-123");

        // Assert
        assertEquals(2, result.size());
        assertEquals(500, result.get(0).getScore());
    }

    @Test
    void testFindTopNPlayersByScoresAsc() {
        // Arrange
        int numberOfRecords = 5;
        List<TopScoresEntity> topScores = Arrays.asList(
                new TopScoresEntityImpl(1, "test-1" , 400),
                new TopScoresEntityImpl(2, "test-2" , 500)
        );
        when(scoreDetailsRepository.findTopNPlayersByScoreAsc(numberOfRecords)).thenReturn(topScores);

        // Act
        List<TopScoresEntity> result = gameService.findTopNPlayersByScores(numberOfRecords, false, "request-123");

        // Assert
        assertEquals(2, result.size());
        assertEquals(400, result.get(0).getScore());
    }

}
