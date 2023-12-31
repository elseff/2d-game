package com.elseff.game;

public enum GameScreenDimension {
    SMALL(854, 480),
    MEDIUM(1280, 720),
    LARGE(1920, 1080);

    private final int width;
    private final int height;

    GameScreenDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
