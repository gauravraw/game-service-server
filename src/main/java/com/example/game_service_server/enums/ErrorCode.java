package com.example.game_service_server.enums;

import lombok.Getter;

public enum ErrorCode {

    UNABLE_TO_SAVE_RECORDS(108431, "Unable to save records in database"),
    INVALID_PLAYER_STATUS(108432, "Invalid player status .Should be ACTIVE OR INACTIVE"),
    EMPTY_NAME(108433, "Invalid player name .Should not be null or blank"),
    UNABLE_TO_READ_FILE(108434, "Unable to read file"),
    UNABLE_TO_SAVE_SCORE(108435,
            "Unable to save scores for any playerId. Please add them before registering-their scores"),
    UNABLE_TO_PROCESS_KAFKA_RECORD(108436, "Unable to process kafka record"),
    INVALID_REQUEST(108437, "Invalid request");

    @Getter
    private final String errorMsg;

    @Getter
    private final int code;

    ErrorCode(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }
}
