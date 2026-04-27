package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Strategies;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Ant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface SimulationStrategy {
    void executeSteps(List<Ant> ants, ConcurrentHashMap<Long, Boolean> blackCells, int steps, int gridSize);
    default void cleanup() {}
}