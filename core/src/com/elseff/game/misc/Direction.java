package com.elseff.game.misc;

public enum Direction {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, 1),
    DOWN(0, -1),
    IDLE(0, 0);

    private final int vx;
    private final int vy;

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

    Direction(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }
}
