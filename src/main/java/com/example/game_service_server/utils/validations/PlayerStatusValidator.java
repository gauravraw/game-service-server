package com.example.game_service_server.utils.validations;

import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.enums.PlayerStatus;
import com.example.game_service_server.exception.GameServiceException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlayerStatusValidator implements ConstraintValidator<ValidPlayerStatus, String> {
    @Override
    public boolean isValid(String playerStatus, ConstraintValidatorContext constraintValidatorContext) {

        try {
            PlayerStatus.valueOf(playerStatus);

        } catch (IllegalArgumentException exception) {
            throw new GameServiceException(ErrorCode.INVALID_PLAYER_STATUS);
        }
        return true;
    }
}
