package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Results {
    private final String mode;
    private final int antCount;
    private final int gridSize;
    private final long totalSteps;
    private final double efficiency;   // ms per step
    private final double speedup;      // Sequential_Time / Parallel_Time
    private final double throughput;   // Steps per second
    private final String timestamp;

    public Results(String mode, int antCount, int gridSize, long totalSteps, double efficiency, double speedup) {
        this.mode = mode;
        this.antCount = antCount;
        this.gridSize = gridSize;
        this.totalSteps = totalSteps;
        this.efficiency = efficiency;
        this.speedup = speedup;
        this.throughput = (efficiency > 0) ? (1000.0 / efficiency) : 0;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Ants: %d | Speedup: %.2fx | Throughput: %,.0f steps/s",
                timestamp, mode, antCount, speedup, throughput);
    }

    public String toCsvRow() {
        return String.format("%s,%s,%d,%d,%d,%.6f,%.2f,%.2f",
                timestamp, mode, antCount, gridSize, totalSteps, efficiency, speedup, throughput);
    }

    public String getMode() { return mode; }
    public int getAntCount() { return antCount; }
    public double getEfficiency() { return efficiency; }
    public double getSpeedup() { return speedup; }
}