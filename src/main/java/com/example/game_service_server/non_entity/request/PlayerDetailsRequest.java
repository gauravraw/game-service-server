package com.example.game_service_server.non_entity.request;

import com.example.game_service_server.utils.validations.ValidPlayerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDetailsRequest {
    @NotBlank(message = "Invalid player name .Should not be null or blank")
    private String name;
    private String team;
    @ValidPlayerStatus
    // @Pattern(regexp = "ACTIVE|INACTIVE" , message = "Invalid player status .Should be ACTIVE OR INACTIVE")
    private String playerStatus;
}
