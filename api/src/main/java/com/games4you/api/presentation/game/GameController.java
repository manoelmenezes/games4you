package com.games4you.api.presentation.game;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import com.games4you.api.application.game.GameService;
import com.games4you.api.application.game.MoveCmd;
import com.games4you.api.application.game.NewGameCmd;
import com.games4you.api.domain.model.game.Game;
import com.games4you.api.domain.model.game.GameNotFoundException;
import com.games4you.api.domain.model.game.GameRepository;
import com.games4you.api.domain.model.game.PlayerUnavailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;

@RestController
public class GameController {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);
    private final GameRepository repository;

    private final GameService gameService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private ChannelTopic topic;

    @Autowired
    private ObjectMapper mapper;

    public GameController(GameRepository repository, GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/games")
    public List<Game> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/games")
    public Game newGame(@RequestBody NewGameCmd cmd) {
      return gameService.newGame(cmd).orElseThrow(() -> new PlayerUnavailableException());
    }

    // Single item
  
    @GetMapping("/games/{id}")
    public Game one(@PathVariable Long id) {    
        return repository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
    }

    @PutMapping("/games/{id}")
    public Game replaceGame(@RequestBody Game newGame, @PathVariable Long id) {
    
    return repository.findById(id)
      .map(game -> {
        game.setBoard(newGame.getBoard());
        return repository.save(game);
      })
      .orElseGet(() -> {
        return repository.save(newGame);
      });
  }

  @DeleteMapping("/games/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }

  @MessageMapping("/game/{gameId}/player/{playerId}")
  @SendTo("/topic/messages/game/{gameId}/player/{playerId}")
  public Game move(@DestinationVariable String gameId, @DestinationVariable String playerId, MoveCmd cmd) {
      Game game = repository.findById(cmd.getGameId()).get();

      Long theOtherPlayerId = game.getBlackPlayerId().equals(Long.valueOf(playerId)) ? game.getWhitePlayerId() : game.getBlackPlayerId();
      game.setCurrentPlayerId(theOtherPlayerId);
      
      try {
	      String jsonMessage = mapper.writeValueAsString(game);
	      if (jsonMessage == null || jsonMessage.trim().isEmpty()) {
		log.info("jsonMessage is empty or null.");
	      } else {
		log.info("json Message bellow:");
	      	log.info(jsonMessage);
	      }
      
      
      		// This dynamically pushes to the topic. If a client is listening, they receive .
	      log.info("Send json message using redis: ", jsonMessage);
	      redisTemplate.convertAndSend(topic.getTopic(), game);
	      log.info("message sent");
	} catch (Exception e) {
		log.error("Fail to convert game to json:  ", e.toString());
	}

      return game;
  }
}

