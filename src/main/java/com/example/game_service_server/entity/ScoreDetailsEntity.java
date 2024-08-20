package com.example.game_service_server.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "score_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreDetailsEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "player_id", unique = true, nullable = false)
    private Integer playerId;

    @Column(name = "score")
    @Builder.Default
    private int score = 0;
}
