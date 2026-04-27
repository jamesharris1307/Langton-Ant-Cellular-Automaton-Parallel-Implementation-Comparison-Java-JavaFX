package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Controllers;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers.BenchmarkManager;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers.SimulationManager;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Results;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities.Camera;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities.InputParser;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities.SimulationRenderer;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities.EvaluationRunner;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class Main_Controller {

    @FXML private Canvas simulationCanvas;
    @FXML private Label modeLabel, performanceLabel, currentAntCountLabel;
    @FXML private Slider speedSlider;
    @FXML private TextField antCountField, gridSizeField, stepsField;
    @FXML private VBox resultsContainer;

    private final SimulationManager simManager = new SimulationManager();
    private final BenchmarkManager benchManager = new BenchmarkManager();
    private final SimulationRenderer renderer = new SimulationRenderer();
    private final Camera camera = new Camera();

    private AnimationTimer timer;
    private boolean isRunning = false;

    @FXML
    public void initialize() {
        setupInteractions();
        setupTimer();
    }

    private void setupTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    runTick();
                    renderer.render(simulationCanvas, simManager, camera);
                }
            }
        };
    }

    private void runTick() {
        int stepsPerFrame = (int) speedSlider.getValue();
        long currentTotal = simManager.getTotalStepsRun();
        long maxSteps = InputParser.getLong(stepsField.getText(), Long.MAX_VALUE);

        if (currentTotal + stepsPerFrame >= maxSteps) {
            int remaining = (int) (maxSteps - currentTotal);
            if (remaining > 0) simManager.update(remaining);
            handleStop();
            return;
        }

        simManager.update(stepsPerFrame);

        double totalTime = simManager.getLastExecutionTimeMillis();
        double normalizedMetric = totalTime / stepsPerFrame;
        performanceLabel.setText(String.format("%.5f ms/step", normalizedMetric));
    }

    @FXML
    private void handleStart() {
        if (isRunning) return;
        if (simManager.getAnts().isEmpty()) handleReset();

        if (!simManager.getAnts().isEmpty()) {
            isRunning = true;
            timer.start();
        }
    }

    @FXML
    private void handleStop() {
        if (!isRunning) return;
        isRunning = false;
        timer.stop();

        if (simManager.getTotalStepsRun() > 0) {
            recordResults();
        }
    }

    @FXML
    private void handleReset() {
        stopSimulation();

        int grid = InputParser.getInt(gridSizeField.getText(), 200);
        int ants = InputParser.getInt(antCountField.getText(), 1);

        simManager.reset(grid, ants);
        currentAntCountLabel.setText(String.valueOf(ants));
        renderer.render(simulationCanvas, simManager, camera);
    }

    @FXML
    private void handleToggleMode() {
        String next = switch (modeLabel.getText()) {
            case "SEQUENTIAL" -> "FORKJOIN";
            case "FORKJOIN" -> "EXECUTOR_FAST";
            case "EXECUTOR_FAST" -> "EXECUTOR_SLOW";
            default -> "SEQUENTIAL";
        };
        modeLabel.setText(next);
        simManager.setMode(next);
    }

    @FXML
    private void handleAutomatedEvaluation() {
        resultsContainer.setDisable(true);
        performanceLabel.setText("Benchmarking...");

        EvaluationRunner runner = new EvaluationRunner(simManager, benchManager);

        Thread evalThread = new Thread(() -> {
            runner.runFullEvaluation();

            Platform.runLater(() -> {
                resultsContainer.setDisable(false);
                performanceLabel.setText("Benchmark Complete");
                refreshResultsUI();
                new Alert(Alert.AlertType.INFORMATION, "Automated Evaluation Finished!").show();
            });
        });

        evalThread.setDaemon(true);
        evalThread.start();
    }

    private void recordResults() {
        double totalTime = simManager.getTotalExecutionTimeMillis();
        long totalSteps = simManager.getTotalStepsRun();
        double averageEfficiency = (totalSteps > 0) ? (totalTime / totalSteps) : 0;

        Results res = new Results(
                modeLabel.getText(),
                simManager.getAnts().size(),
                simManager.getGridSize(),
                totalSteps,
                averageEfficiency,
                1.0
        );

        benchManager.addResult(res);
        addResultToUI(res);
    }

    private void addResultToUI(Results res) {
        Label label = new Label(res.toString());
        label.setWrapText(true);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.setStyle("-fx-text-fill: #00ff00; -fx-padding: 8; -fx-border-color: #444; " +
                "-fx-background-color: #1e1e1e; -fx-font-family: 'Consolas'; -fx-font-size: 11px; " +
                "-fx-border-radius: 4; -fx-background-radius: 4;");
        resultsContainer.getChildren().add(0, label);
    }

    private void refreshResultsUI() {
        resultsContainer.getChildren().clear();
        for (Results res : benchManager.getResults()) {
            addResultToUI(res);
        }
    }

    @FXML
    public void handleExportResults() {
        if (benchManager.isEmpty()) return;

        FileChooser fc = new FileChooser();
        fc.setInitialFileName("ant_benchmarks.csv");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fc.showSaveDialog(simulationCanvas.getScene().getWindow());
        if (file != null) {
            try {
                benchManager.exportToCSV(file);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Export Failed: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleClearResults() {
        resultsContainer.getChildren().clear();
        benchManager.clear();
    }

    private void setupInteractions() {
        camera.reset();

        simulationCanvas.setOnScroll(event -> {
            camera.zoomAt((event.getDeltaY() > 0) ? 1.1 : 0.9, event.getX(), event.getY());
            if (!isRunning) renderer.render(simulationCanvas, simManager, camera);
        });

        final double[] lastMouse = new double[2];
        simulationCanvas.setOnMousePressed(e -> {
            lastMouse[0] = e.getX(); lastMouse[1] = e.getY();
        });
        simulationCanvas.setOnMouseDragged(e -> {
            camera.move(e.getX() - lastMouse[0], e.getY() - lastMouse[1]);
            lastMouse[0] = e.getX(); lastMouse[1] = e.getY();
            if (!isRunning) renderer.render(simulationCanvas, simManager, camera);
        });
    }

    private void stopSimulation() {
        isRunning = false;
        if (timer != null) timer.stop();
        performanceLabel.setText("0.00 ms");
        camera.reset();
    }
}