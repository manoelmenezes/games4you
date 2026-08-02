package com.games4you.api.domain.model.game;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PlayersQueue {

    @GeneratedValue
    @Id
    private Long id;

    private Long playerId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public PlayersQueue() {

    }

    public PlayersQueue(Long playersId) {
        this.playerId = playersId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayersQueue)) {
            return false;
        }
        PlayersQueue other = (PlayersQueue) o;
        return Objects.equals(this.id, other.id) && Objects.equals(this.playerId, other.playerId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.playerId);
    }

    @Override
    public String toString() {
        return "PlayersQueue{id=" + this.id + ", playerId=" + this.playerId + "}";
    }
    
}
