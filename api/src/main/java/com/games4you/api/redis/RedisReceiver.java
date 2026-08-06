package com.games4you.api.redis;

import com.games4you.api.domain.model.game.Game;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class RedisReceiver {
    private static final Logger log = LoggerFactory.getLogger(RedisReceiver.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    public RedisReceiver(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
	this.objectMapper = objectMapper;
    }
    public void receiveMessage(String gameJson) {
	    Game game = null;
	    try {
             game = objectMapper.readValue(gameJson, Game.class);
	    } catch (Exception e) {
		    log.error("Fail to parse game json: ", gameJson);
		    return;
	    }
	    log.info("Game id:", game.getId(), " Current:", game.getCurrentPlayerId());
       String destination = "/topic/messages/game/" + game.getId() + "/player/" + game.getCurrentPlayerId();

      // This dynamically pushes to the topic. If a client is listening, they receive it.
      messagingTemplate.convertAndSend(destination, game);
    }
}
