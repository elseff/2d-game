package com.elseff.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.elseff.game.util.GameScreenDimension;

public class DesktopLauncher {
    public static void main(String[] arg) {
        GameScreenDimension dimension = GameScreenDimension.MEDIUM;

        int width = dimension.getWidth();
        int height = dimension.getHeight();

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setDecorated(false);
        config.setWindowedMode(width, height);
        config.setForegroundFPS(60);
        config.setTitle("Game");
        new Lwjgl3Application(new MyGdxGame(width, height), config);
    }
}
