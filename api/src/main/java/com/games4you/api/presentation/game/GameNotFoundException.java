package com.games4you.api.presentation.game;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("Could not found game " + id);
    }
    
}
