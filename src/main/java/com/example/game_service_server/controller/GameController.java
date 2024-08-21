package com.example.game_service_server.controller;

import com.example.game_service_server.entity.TopScoresEntity;
import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.request.PlayerScore;
import com.example.game_service_server.non_entity.request.PlayerDetailsRequest;
import com.example.game_service_server.non_entity.response.BaseResponse;
import com.example.game_service_server.non_entity.response.PlayerDetailsResponse;
import com.example.game_service_server.non_entity.response.ScoreAdditionResponse;
import com.example.game_service_server.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.game_service_server.utils.CommonUtils.readFile;

@RestController
@RequestMapping("/v1/game")
@RequiredArgsConstructor
@Slf4j
@Validated
public class GameController {

    private final GameService gameService;

    @PostMapping("/save/players")
    public ResponseEntity<BaseResponse<List<PlayerDetailsResponse>>> savePlayerDetails(
            @RequestParam("x-request-id") String requestId,
            @Valid @RequestBody List<PlayerDetailsRequest> playerDetailsRequestList) {
        List<PlayerDetailsResponse> playerDetailsResponseList = gameService.addPlayers(requestId,
                playerDetailsRequestList);

        return ResponseEntity.ok(BaseResponse.<List<PlayerDetailsResponse>> builder().success(true)
                .data(playerDetailsResponseList).build());
    }

    @PostMapping("/save/scores")
    public ResponseEntity<BaseResponse<ScoreAdditionResponse>> saveScores(@RequestParam("x-request-id") String requestId,
                                                                          @RequestParam("file") MultipartFile multipartFile) {
        List<PlayerScore> playerScoreList = readFile(multipartFile);
        ScoreAdditionResponse scoreAdditionResponse = gameService.addScores(playerScoreList, requestId);
        return ResponseEntity.ok(BaseResponse.<ScoreAdditionResponse> builder().success(true).data(scoreAdditionResponse).build());
    }

    @GetMapping("/top/score")
    public ResponseEntity<BaseResponse<List<TopScoresEntity>>> getTopScore(
            @RequestParam("x-request-id") String requestId,
            @RequestParam(value = "numberOfRecords", required = false, defaultValue = "5") int numberOfRecords,
            @RequestParam(value = "orderByDesc", required = false, defaultValue = "true") boolean orderByDesc) {
        if(numberOfRecords <= 0){
            throw new GameServiceException(ErrorCode.INVALID_REQUEST);
        }
        List<TopScoresEntity> scoresEntityList = gameService.findTopNPlayersByScores(numberOfRecords, orderByDesc,
                requestId);

        return ResponseEntity
                .ok(BaseResponse.<List<TopScoresEntity>> builder().data(scoresEntityList).success(true).build());
    }
}
