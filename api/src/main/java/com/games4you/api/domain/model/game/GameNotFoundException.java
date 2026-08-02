package com.games4you.api.domain.model.game;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("Could not found game " + id);
    }
    
}
