package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Ant;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Strategies.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationManager {
    private final ConcurrentHashMap<Long, Boolean> blackCells = new ConcurrentHashMap<>();
    private final List<Ant> ants = new ArrayList<>();

    private long totalStepsRun = 0;
    private long lastExecutionTimeNano = 0;
    private long cumulativeTimeNano = 0;
    private int gridSize;
    private String currentMode = "SEQUENTIAL";

    private final Sequential sequential = new Sequential();
    private final ExecutorServiceStrategy executor = new ExecutorServiceStrategy();
    private final ForkJoinPoolStrategy forkJoin = new ForkJoinPoolStrategy();

    public SimulationManager() {
        initialiseStrategies();
    }

    private void initialiseStrategies() {
        executor.initialise();
        forkJoin.initialise();
    }

    private void cleanupStrategies() {
        sequential.cleanup();
        executor.cleanup();
        forkJoin.cleanup();
    }

    public void setMode(String mode) {
        if (mode != null) {
            this.currentMode = mode.toUpperCase();
        }
    }

    public void reset(int gridSize, int antCount) {
        cleanupStrategies();
        initialiseStrategies();

        this.gridSize = gridSize;
        this.totalStepsRun = 0;
        this.lastExecutionTimeNano = 0;
        this.cumulativeTimeNano = 0;

        blackCells.clear();
        ants.clear();

        int center = gridSize / 2;
        for (int i = 0; i < antCount; i++) {
            ants.add(new Ant(center, center));
        }
    }

    public void update(int steps) {
        long start = System.nanoTime();

        runSelectedStrategy(steps);

        long duration = System.nanoTime() - start;
        this.lastExecutionTimeNano = duration;
        this.cumulativeTimeNano += duration;
        this.totalStepsRun += steps;
    }

    private void runSelectedStrategy(int steps) {
        switch (currentMode) {
            case "EXECUTOR_SLOW" -> executor.executeNotBatching(ants, blackCells, steps, gridSize);
            case "EXECUTOR_FAST" -> executor.executeBatching(ants, blackCells, steps, gridSize);
            case "FORKJOIN"      -> forkJoin.executeSteps(ants, blackCells, steps, gridSize);
            default              -> sequential.executeSteps(ants, blackCells, steps, gridSize);
        }
    }

    public double getLastExecutionTimeMillis() { return lastExecutionTimeNano / 1_000_000.0; }

    public double getTotalExecutionTimeMillis() { return cumulativeTimeNano / 1_000_000.0; }

    public long getTotalStepsRun() { return totalStepsRun; }
    public ConcurrentHashMap<Long, Boolean> getBlackCells() { return blackCells; }
    public List<Ant> getAnts() { return ants; }
    public int getGridSize() { return gridSize; }
}