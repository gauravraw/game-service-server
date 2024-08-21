package com.example.game_service_server.handler;

import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.response.BaseResponse;
import com.example.game_service_server.non_entity.response.Error;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationExceptionNew(
            ConstraintViolationException constraintViolationException) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setSuccess(false);
        baseResponse.setData(null);
        StringBuilder errorMessage = new StringBuilder();
        constraintViolationException.getConstraintViolations().forEach(constraintViolation -> {
            errorMessage.append(constraintViolation.getMessage()).append(",");
        });
        errorMessage.deleteCharAt(errorMessage.length() - 1);
        baseResponse.setError(
                Error.builder().code(ErrorCode.INVALID_REQUEST.getCode()).errorMsg(errorMessage.toString()).build());
        return ResponseEntity.ok(baseResponse);
    }
}
