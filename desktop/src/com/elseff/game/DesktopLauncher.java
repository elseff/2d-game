package com.elseff.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setDecorated(false);
        config.setWindowedMode(1920,1080);
        config.setResizable(false);
        config.setForegroundFPS(60);
        config.setTitle("Game");
        new Lwjgl3Application(new MyGdxGame(), config);
    }
}
