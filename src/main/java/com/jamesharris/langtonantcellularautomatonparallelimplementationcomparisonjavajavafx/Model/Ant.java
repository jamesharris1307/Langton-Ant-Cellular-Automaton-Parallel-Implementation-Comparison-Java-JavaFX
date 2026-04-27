package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Model;

import java.util.concurrent.ConcurrentHashMap;

public class Ant {
    private int x, y;
    private int direction = 0;

    public Ant(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void turnClockwise() { direction = (direction + 1) % 4; }
    public void turnCounterClockwise() { direction = (direction + 3) % 4; }

    public void move() {
        if (direction == 0) y--;
        else if (direction == 1) x++;
        else if (direction == 2) y++;
        else if (direction == 3) x--;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction % 4;
    }

    public void step(ConcurrentHashMap<Long, Boolean> cells, int gridSize) {
        long key = ((long) x << 32) | (y & 0xFFFFFFFFL);

        final boolean[] wasBlack = new boolean[1];

        cells.compute(key, (k, v) -> {
            wasBlack[0] = (v != null && v);
            return !wasBlack[0];
        });

        if (wasBlack[0]) {
            turnCounterClockwise();
        } else {
            turnClockwise();
        }

        move();

        if (x < 0) x = gridSize - 1;
        else if (x >= gridSize) x = 0;
        if (y < 0) y = gridSize - 1;
        else if (y >= gridSize) y = 0;
    }
}