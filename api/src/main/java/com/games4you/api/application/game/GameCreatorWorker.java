package com.games4you.api.application.game;

import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.games4you.api.domain.model.game.Game;
import com.games4you.api.domain.model.game.GameRepository;
import com.games4you.api.domain.model.game.PlayersQueue;
import com.games4you.api.domain.model.game.PlayersQueueRepository;

@Component
public class GameCreatorWorker implements Runnable {

    private static final String NEW_GAME_BOARD_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static final Logger log = LoggerFactory.getLogger(GameCreatorWorker.class);
    private static final int SLEEP_TIME_MS = 1000;

    private final PlayersQueueRepository playersQueueRepository;
    private final GameRepository gameRepository;

    public GameCreatorWorker(PlayersQueueRepository playersQueueRepository, GameRepository gameRepository) {
        this.playersQueueRepository = playersQueueRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void run() {
        log.info("GameCreatorWorker thread started and running...");
        
        // Keep the thread alive as long as it is not interrupted
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Your continuous business logic goes here
                createGame();

                // Prevent 100% CPU utilization by adding a controlled pause
                Thread.sleep(SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                log.info("Worker thread interrupted. Shutting down gracefully...");
                // Restore interrupted status to cleanly exit the loop
                Thread.currentThread().interrupt(); 
            } catch (Exception e) {
                log.error("Error occurred during task execution", e);
            }
        }
    }

    @Transactional
    private void createGame() {
        log.info("Processing ongoing background work...");

        PlayersQueue pq1 = getPlayer();
        PlayersQueue pq2 = getPlayer();

        Game game = new Game(pq1.getPlayerId(), pq2.getPlayerId(), NEW_GAME_BOARD_FEN);
        gameRepository.save(game);
        
        playersQueueRepository.delete(pq1);
        playersQueueRepository.delete(pq2);
    }

    private PlayersQueue getPlayer() {
        Optional<PlayersQueue> pq = Optional.empty();
        do {
            try {
                pq = playersQueueRepository.findForUpdate();
                Thread.sleep(SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                log.info("Worker thread interrupted. Shutting down gracefully...");
                // Restore interrupted status to cleanly exit the loop
                Thread.currentThread().interrupt(); 
            }
        } while (!Thread.currentThread().isInterrupted() && pq.isEmpty());

        if (pq.isEmpty()) {
            throw new RuntimeException("Player not found");
        }

        return pq.get();        
    }
    
}
