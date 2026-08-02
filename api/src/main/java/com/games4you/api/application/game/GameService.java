package com.games4you.api.application.game;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.games4you.api.domain.model.game.Game;
import com.games4you.api.domain.model.game.GameRepository;
import com.games4you.api.domain.model.game.PlayersQueue;
import com.games4you.api.domain.model.game.PlayersQueueRepository;

@Service
public class GameService {

    private static final int MAX_RETRIES = 10;
    private static final int SLEEP_TIME_MS = 1000;
    
    private final GameRepository gameRepository;
    private final PlayersQueueRepository playersQueueRepository;

    public GameService(GameRepository gameRepository, PlayersQueueRepository playersQueueRepository) {
        this.gameRepository = gameRepository;
        this.playersQueueRepository = playersQueueRepository;
    }

    public Optional<Game> newGame(NewGameCmd cmd) {
        PlayersQueue pq = new PlayersQueue(cmd.getPlayerId());
        playersQueueRepository.save(pq);

        return findNewGame(cmd.getPlayerId());
    }

    private Optional<Game> findNewGame(Long playerId) {
        int retry = 0;
        Optional<Game> game = Optional.empty();
        do {
            game = gameRepository.getCreatedGameByPlayerId(playerId);
            retry++;
            if (game.isEmpty()) {                
                try {
                    Thread.sleep(SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); 
                }
            }
        } while (!Thread.currentThread().isInterrupted() && game.isEmpty() && retry < MAX_RETRIES);

        return game;
    }

}
