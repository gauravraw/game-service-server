package com.example.game_service_server.listener;

import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.request.PlayerScore;
import com.example.game_service_server.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerListener {

    private final ObjectMapper objectMapper;

    private final GameService gameService;

    @KafkaListener(topics = "test-topic")
    public void listenMessage(String message) {
        try {
            log.info("Received kafka message as :: {}", message);
            log.info("Mapping kafka object to custom object class");
            PlayerScore playerScore = objectMapper.readValue(message, PlayerScore.class);
            if(playerScore!= null){
                log.info("Sending data for processing");
                String requestId = UUID.randomUUID().toString();
                gameService.addScores(List.of(playerScore), requestId);
            }
        } catch (GameServiceException gameServiceException) {
            log.error("Error occurred while processing kafka message", gameServiceException);
            throw gameServiceException;
        } catch (IOException e) {
            log.error("Error occurred while processing kafka message", e);
            throw new GameServiceException(ErrorCode.UNABLE_TO_PROCESS_KAFKA_RECORD);
        }
    }
}
