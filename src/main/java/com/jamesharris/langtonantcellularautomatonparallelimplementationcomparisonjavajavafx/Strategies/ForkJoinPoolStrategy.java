package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Strategies;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Ant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ForkJoinPoolStrategy implements SimulationStrategy {
    private ForkJoinPool pool;

    public ForkJoinPoolStrategy() {
        initialise();
    }

    public void initialise() {
        if (pool == null || pool.isShutdown()) {
            pool = new ForkJoinPool();
        }
    }

    @Override
    public void executeSteps(List<Ant> ants, ConcurrentHashMap<Long, Boolean> blackCells, int steps, int gridSize) {
        if (pool == null || pool.isShutdown()) {
            initialise();
        }

        for (int s = 0; s < steps; s++) {
            pool.invoke(new AntTask(ants, blackCells, 0, ants.size(), gridSize));
        }
    }

    @Override
    public void cleanup() {
        if (pool != null) {
            pool.shutdownNow();
            try {
                pool.awaitTermination(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class AntTask extends RecursiveAction {
        private final List<Ant> ants;
        private final ConcurrentHashMap<Long, Boolean> cells;
        private final int start, end, gridSize;
        private static final int THRESHOLD = 500;

        AntTask(List<Ant> ants, ConcurrentHashMap<Long, Boolean> cells, int start, int end, int gridSize) {
            this.ants = ants;
            this.cells = cells;
            this.start = start;
            this.end = end;
            this.gridSize = gridSize;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    ants.get(i).step(cells, gridSize);
                }
            } else {
                int mid = (start + end) / 2;
                invokeAll(
                        new AntTask(ants, cells, start, mid, gridSize),
                        new AntTask(ants, cells, mid, end, gridSize)
                );
            }
        }
    }
}