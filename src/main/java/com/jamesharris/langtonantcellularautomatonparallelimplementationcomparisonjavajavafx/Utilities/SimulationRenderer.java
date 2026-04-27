package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities;

import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Managers.SimulationManager;
import com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model.Ant;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimulationRenderer {

    public void render(Canvas canvas, SimulationManager manager, Camera camera) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        int grid = manager.getGridSize();

        if (grid <= 0) return;

        double cellSize = Math.min(w / grid, h / grid) * camera.zoom;

        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, w, h);

        drawCells(gc, manager, camera, cellSize, w, h);
        drawAnts(gc, manager, camera, cellSize, w, h);
    }

    private void drawCells(GraphicsContext gc, SimulationManager manager, Camera camera, double size, double w, double h) {
        gc.setFill(Color.WHITE);
        manager.getBlackCells().forEach((key, isBlack) -> {
            if (isBlack) {
                double x = ((key >> 32) * size) + camera.offsetX;
                double y = ((key & 0xFFFFFFFFL) * size) + camera.offsetY;
                if (isVisible(x, y, size, w, h)) gc.fillRect(x, y, size, size);
            }
        });
    }

    private void drawAnts(GraphicsContext gc, SimulationManager manager, Camera camera, double size, double w, double h) {
        gc.setFill(Color.RED);
        for (Ant ant : manager.getAnts()) {
            double x = (ant.getX() * size) + camera.offsetX;
            double y = (ant.getY() * size) + camera.offsetY;
            if (isVisible(x, y, size, w, h)) gc.fillRect(x, y, size, size);
        }
    }

    private boolean isVisible(double x, double y, double size, double w, double h) {
        return x > -size && x < w && y > -size && y < h;
    }
}