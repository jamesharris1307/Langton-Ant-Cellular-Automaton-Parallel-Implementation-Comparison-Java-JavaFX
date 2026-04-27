package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Strategies;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Ant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Sequential implements SimulationStrategy {
    @Override
    public void executeSteps(List<Ant> ants, ConcurrentHashMap<Long, Boolean> blackCells, int steps, int gridSize) {
        for (int s = 0; s < steps; s++) {
            for (Ant ant : ants) {
                ant.step(blackCells, gridSize);
            }
        }
    }
}