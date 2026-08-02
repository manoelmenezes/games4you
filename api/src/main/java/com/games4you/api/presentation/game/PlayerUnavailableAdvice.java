package com.games4you.api.presentation.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.games4you.api.domain.model.game.PlayerUnavailableException;

@RestControllerAdvice
public class PlayerUnavailableAdvice {

    @ExceptionHandler(PlayerUnavailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String playerUnavailableHandler(PlayerUnavailableException ex) {
        return ex.getMessage();
    }
}