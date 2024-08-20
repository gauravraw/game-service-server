package com.example.game_service_server.repository;

import com.example.game_service_server.entity.ScoreDetailsEntity;
import com.example.game_service_server.entity.TopScoresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreDetailsRepository extends JpaRepository<ScoreDetailsEntity, Integer> {

    Optional<ScoreDetailsEntity> findByPlayerId(int playerId);

    boolean existsByPlayerId(int playerId);

    @Query("Select s.playerId AS playerId , p.name AS playerName , s.score AS score From ScoreDetailsEntity s INNER JOIN PlayerDetailsEntity p ON p.id = s.playerId Order By s.score DESC LIMIT :numberOfRecords")
    List<TopScoresEntity> findTopNPlayersByScoreDesc(int numberOfRecords);

    @Query("Select s.playerId AS playerId , p.name AS playerName , s.score AS score From ScoreDetailsEntity s INNER JOIN PlayerDetailsEntity p ON p.id = s.playerId Order By s.score ASC LIMIT :numberOfRecords")
    List<TopScoresEntity> findTopNPlayersByScoreAsc(int numberOfRecords);
}
