package com.example.game_service_server.handler;

import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.response.BaseResponse;
import com.example.game_service_server.non_entity.response.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameServiceExceptionHandler {

    @ExceptionHandler(GameServiceException.class)
    public ResponseEntity<BaseResponse<String>> handleGameServiceException(GameServiceException gameServiceException) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setSuccess(false);
        baseResponse.setData(null);
        baseResponse.setError(Error.builder().code(gameServiceException.getCode())
                .errorMsg(gameServiceException.getErrorMsg()).build());
        return ResponseEntity.ok(baseResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationException(
            MethodArgumentNotValidException constraintViolationException) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setSuccess(false);
        baseResponse.setData(null);
        baseResponse.setError(Error.builder().code(constraintViolationException.getStatusCode().value())
                .errorMsg(constraintViolationException.getMessage()).build());
        return ResponseEntity.ok(baseResponse);
    }
}
