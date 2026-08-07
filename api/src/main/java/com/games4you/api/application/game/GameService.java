package com.games4you.api.application.game;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.games4you.api.domain.model.game.Game;
import com.games4you.api.domain.model.game.GameRepository;
import com.games4you.api.domain.model.game.PlayerUnavailableException;
import com.games4you.api.domain.model.game.PlayersQueue;
import com.games4you.api.domain.model.game.PlayersQueueRepository;

import jakarta.transaction.Transactional;

@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private static final String NEW_GAME_BOARD_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
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
        log.info("Saving player id in queue {}", cmd.getPlayerId());
	playersQueueRepository.save(pq);
	log.info("Player id {} saved in queue", cmd.getPlayerId());

        return findNewGame(cmd.getPlayerId());
    }

    private Optional<Game> findNewGame(Long playerId) {
        int retry = 0;
        Optional<Game> game = Optional.empty();
        do {
            log.info("Trying to find game created for player {}", playerId);
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

    @Transactional
    public void createGame() {
        log.info("Creating game...");

        PlayersQueue pq1 = getPlayer();
        playersQueueRepository.delete(pq1);
	log.info("Found one player {} in queue", pq1.getPlayerId());
        PlayersQueue pq2 = getPlayer();
        playersQueueRepository.delete(pq2);
	log.info("Found another player in queue", pq2.getPlayerId());

        Game game = new Game(pq1.getPlayerId(), pq2.getPlayerId(), NEW_GAME_BOARD_FEN);
        gameRepository.save(game);
    }

    private PlayersQueue getPlayer() {
        Optional<PlayersQueue> pq = Optional.empty();
        do {
            try {
		log.info("Trying to get player from queue");
                pq = playersQueueRepository.findForUpdate();
                Thread.sleep(SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                log.info("Worker thread interrupted. Shutting down gracefully...");
                // Restore interrupted status to cleanly exit the loop
                Thread.currentThread().interrupt(); 
            }
        } while (!Thread.currentThread().isInterrupted() && pq.isEmpty());

        if (pq.isEmpty()) {
	    log.info("No player in queue. Throwing PlayerUnavailableException");
            throw new PlayerUnavailableException();
        }

	log.info("Player present in queue");
        return pq.get();        
    }

}
