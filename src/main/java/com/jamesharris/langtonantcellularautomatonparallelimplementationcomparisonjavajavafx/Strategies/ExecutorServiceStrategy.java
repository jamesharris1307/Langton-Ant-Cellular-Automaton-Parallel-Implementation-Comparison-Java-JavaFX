package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Strategies;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Ant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceStrategy implements SimulationStrategy {
    private ExecutorService executor;

    public void initialise() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void executeNotBatching(List<Ant> ants, ConcurrentHashMap<Long, Boolean> blackCells, int steps, int gridSize) {
        for (int s = 0; s < steps; s++) {
            List<Callable<Void>> tasks = new ArrayList<>();
            for (Ant ant : ants) {
                tasks.add(() -> {
                    ant.step(blackCells, gridSize);
                    return null;
                });
            }
            runTasks(tasks);
        }
    }

    public void executeBatching(List<Ant> ants, ConcurrentHashMap<Long, Boolean> blackCells, int steps, int gridSize) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = Math.max(1, ants.size() / numThreads);

        for (int s = 0; s < steps; s++) {
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < ants.size(); i += chunkSize) {
                int start = i;
                int end = Math.min(i + chunkSize, ants.size());

                tasks.add(() -> {
                    for (int j = start; j < end; j++) {
                        ants.get(j).step(blackCells, gridSize);
                    }
                    return null;
                });
            }
            runTasks(tasks);
        }
    }

    private void runTasks(List<Callable<Void>> tasks) {
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void executeSteps(List<Ant> ants, ConcurrentHashMap<Long, Boolean> blackCells, int steps, int gridSize) {
        executeBatching(ants, blackCells, steps, gridSize);
    }

    public void cleanup() {
        if (executor != null) executor.shutdownNow();
    }
}