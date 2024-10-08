package com.example.game_service_server.repository;

import com.example.game_service_server.entity.PlayerDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerDetailsRepository extends JpaRepository<PlayerDetailsEntity, Integer> {
}
