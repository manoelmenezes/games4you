package com.games4you.api.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.games4you.api.domain.model.Game;
import com.games4you.api.domain.model.GameRepository;

@Configuration
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(GameRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Game("r5k1/ppp3p1/2nbqr1p/4p3/2P1P3/1P2B1Q1/P4PPP/3R1RK1 b - - 0 1")));
            log.info("Preloading " + repository.save(new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")));
    };
  }
    
}
