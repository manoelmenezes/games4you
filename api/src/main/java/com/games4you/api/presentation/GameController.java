package com.games4you.api.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.games4you.api.domain.model.Game;
import com.games4you.api.domain.model.GameRepository;

@RestController
public class GameController {

    private final GameRepository repository;

    public GameController(GameRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/games")
    public List<Game> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/games")
    public Game newGame(@RequestBody Game newGame) {
    return repository.save(newGame);
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
}
