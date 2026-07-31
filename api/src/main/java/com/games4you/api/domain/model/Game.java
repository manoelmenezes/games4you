package com.games4you.api.domain.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Game {

    @GeneratedValue
    @Id
    private Long id;

    private String board;

    public Game() {}

    public Game(String board) {
        this.board = board;
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
