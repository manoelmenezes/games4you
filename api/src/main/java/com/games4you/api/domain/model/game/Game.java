package com.games4you.api.domain.model.game;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Game {

    @GeneratedValue
    @Id
    public Long id;

    public Long whitePlayerId;

    public Long blackPlayerId;

    public Long currentPlayerId;

    public String board;

    public Game() {}

    public Game(Long whitePlayerId, Long blackPlayerId, String board) {
        this.board = board;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
    }

    public Long getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(Long blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    public Long getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(Long whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Long getCurrentPlayerId() {
	return currentPlayerId;
    }

    public void setCurrentPlayerId(Long currentPlayerId) {
	this.currentPlayerId = currentPlayerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        Game other = (Game) o;
        return Objects.equals(this.id, other.id) && Objects.equals(this.board, other.board);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.board);
    }

    @Override
    public String toString() {
        return "Game{id=" + this.id + ", board=" + this.board + "}";
    }

}
