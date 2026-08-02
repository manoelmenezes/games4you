package com.games4you.api.application.game;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class GameCreatorCmdLineRunner {

    private static final int POOL_SIZE = 3;

     @Bean
     CommandLineRunner startGameCreatorWorkers(GameCreatorWorker worker) {
        return args -> {
                // Create an executor pool to manage your worker threads
            ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
            
            // Submit the tasks to start running indefinitely
            for (int i = 0; i < POOL_SIZE; i++) {
                executor.submit(worker);
            }
            
            // Note: Do not call executor.shutdown() here if you want them to live forever.
            // Spring will handle the application lifecycle.
        };
    }
}
