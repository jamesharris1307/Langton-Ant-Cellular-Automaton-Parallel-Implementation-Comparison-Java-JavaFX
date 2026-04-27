package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Results;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkManager {
    private final List<Results> benchmarkList = new ArrayList<>();

    public void addResult(Results res) {
        benchmarkList.add(0, res);
    }

    public void clear() {
        benchmarkList.clear();
    }

    public boolean isEmpty() {
        return benchmarkList.isEmpty();
    }

    public void exportToCSV(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Timestamp,Mode,AntCount,GridSize,TotalSteps,MsPerStep,Speedup,ThroughputStepsPerSec");

            for (Results res : benchmarkList) {
                writer.println(res.toCsvRow());
            }
        }
    }

    public List<Results> getResults() {
        return new ArrayList<>(benchmarkList);
    }
}