package com.games4you.api.presentation.game;

public class PlayerUnavailableException extends RuntimeException {

    public PlayerUnavailableException() {
        super("No other player is available to play.");
    }
    
}
