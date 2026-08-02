package com.games4you.api.domain.model.game;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT p FROM Game g WHERE g.whitePlayerId = :playerId OR g.blackPlayerId = :playerId")
    Optional<Game> getCreatedGameByPlayerId(@Param("playerId")Long playerId);
    
}
