package com.example.game_service_server.exception;

import com.example.game_service_server.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GameServiceException extends RuntimeException {

    private int code;
    private String errorMsg;

    public GameServiceException(ErrorCode error) {
        this.code = error.getCode();
        this.errorMsg = error.getErrorMsg();
    }

    public GameServiceException(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }
}
