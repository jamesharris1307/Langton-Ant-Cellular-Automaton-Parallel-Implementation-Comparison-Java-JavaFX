package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers.BenchmarkManager;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers.SimulationManager;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Results;
import javafx.application.Platform;

public class EvaluationRunner {
    private final SimulationManager simManager;
    private final BenchmarkManager benchManager;

    private final int[] ANT_TEST_COUNTS = {1, 10, 100, 1000, 10000, 100000};
    private final String[] PARALLEL_MODES = {"FORKJOIN", "EXECUTOR_FAST", "EXECUTOR_SLOW"};
    private final int GRID_SIZE = 10000;
    private final int WARMUP_STEPS = 2000;
    private final int EVAL_STEPS = 10000;

    public EvaluationRunner(SimulationManager simManager, BenchmarkManager benchManager) {
        this.simManager = simManager;
        this.benchManager = benchManager;
    }

    public void runFullEvaluation() {
        System.out.println(">>> STARTING AUTOMATED PERFORMANCE EVALUATION <<<");

        for (int antCount : ANT_TEST_COUNTS) {
            System.out.println("\nTesting Population Size: " + antCount);

            double seqEfficiency = runTest("SEQUENTIAL", antCount, 1.0);

            for (String mode : PARALLEL_MODES) {
                double parallelEfficiency = runTest(mode, antCount, seqEfficiency);
            }
        }

        System.out.println("\n>>> EVALUATION COMPLETE. EXPORT CSV FOR ANALYSIS <<<");
    }

    private double runTest(String mode, int antCount, double seqEfficiencyForSpeedup) {
        simManager.reset(GRID_SIZE, antCount);
        simManager.setMode(mode);

        simManager.update(WARMUP_STEPS);

        long start = System.nanoTime();
        simManager.update(EVAL_STEPS);
        long duration = System.nanoTime() - start;

        double efficiency = (duration / 1_000_000.0) / EVAL_STEPS;

        double speedup = (mode.equals("SEQUENTIAL")) ? 1.0 : (seqEfficiencyForSpeedup / efficiency);

        Results res = new Results(mode, antCount, GRID_SIZE, EVAL_STEPS, efficiency, speedup);

        benchManager.addResult(res);

        System.out.printf("   [%s] Efficiency: %.6f ms/step | Speedup: %.2fx%n",
                mode, efficiency, speedup);

        return efficiency;
    }
}