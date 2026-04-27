package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities;

public class Camera {

    public double zoom = 1.0;
    public double offsetX = 0;
    public double offsetY = 0;

    public void move(double dx, double dy) {
        offsetX += dx;
        offsetY += dy;
    }

    public void zoomAt(double factor, double mouseX, double mouseY) {

        double oldZoom = zoom;
        zoom *= factor;

        offsetX = mouseX - ((mouseX - offsetX) * (zoom / oldZoom));
        offsetY = mouseY - ((mouseY - offsetY) * (zoom / oldZoom));
    }

    public void reset() {
        zoom = 1.0;
        offsetX = 0;
        offsetY = 0;
    }
}