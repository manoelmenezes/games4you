package com.games4you.api.domain.model.game;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlayersQueueRepository extends JpaRepository<PlayersQueue, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PlayesQueue p ORDER BY createdAt DESC LIMIT 1")
    Optional<PlayersQueue> findForUpdate();

}