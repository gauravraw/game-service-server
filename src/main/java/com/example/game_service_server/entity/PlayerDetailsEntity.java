package com.example.game_service_server.entity;

import com.example.game_service_server.enums.PlayerStatus;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "player_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDetailsEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "team")
    private String team;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;
}
