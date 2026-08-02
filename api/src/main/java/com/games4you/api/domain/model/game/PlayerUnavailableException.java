package com.games4you.api.domain.model.game;

public class PlayerUnavailableException extends RuntimeException {

    public PlayerUnavailableException() {
        super("No other player is available to play.");
    }
    
}
