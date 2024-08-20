package com.example.game_service_server.non_entity.request;

import com.example.game_service_server.utils.validations.ValidPlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDetailsRequest {
    private String name;
    private String team;
    @ValidPlayerStatus
    private String playerStatus;
}
