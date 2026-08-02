package com.games4you.api.application.game;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GameCreatorCmdLineRunner implements CommandLineRunner {

    private static final int POOL_SIZE = 3;

    private final GameCreatorWorker worker;

    // Inject your runnable worker task
    public GameCreatorCmdLineRunner(GameCreatorWorker worker) {
        this.worker = worker;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create an executor pool to manage your worker threads
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        
        // Submit the tasks to start running indefinitely
        for (int i = 0; i < POOL_SIZE; i++) {
            executor.submit(worker);
        }
        
        // Note: Do not call executor.shutdown() here if you want them to live forever.
        // Spring will handle the application lifecycle.
    }
}
