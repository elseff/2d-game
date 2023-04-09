package com.elseff.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.elseff.game.MyGdxGame;

/**
 * Some windows utilities
 * such as additional info about the game and grid
 */
public class WindowUtil {
    private final MyGdxGame game;
    private final int margin = 20; // 20 by default
    private final int padding = 5; // 5 by default
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final String[] data; // info tab data

    public WindowUtil(MyGdxGame game) {
        this.game = game;
        this.batch = new SpriteBatch(); // own batch

        this.data = new String[7]; // info with 7 lines
        this.data[0] = "DEBUG MODE"; // first line in info tab is title of debug mode
        this.font = new BitmapFont();
        this.font.setColor(Color.GREEN);
    }

    public void render() {
        update();
        info();
    }

    public void update() {
        data[1] = String.format("fps: %s",
                Gdx.graphics.getFramesPerSecond()); // second line if spf
        data[2] = String.format("delta (1/fps): %f",
                Gdx.graphics.getDeltaTime()); // third line is current delta time
        data[3] = String.format("dimension: %s x %s",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()); // fourth line is current screen dimension
        data[4] = String.format("time: %.1f s.",
                game
                        .getTime()); // fifth line is time since game starting
        data[5] = String.format("count chunks: %.1s.",
                game
                        .getScreen()
                        .getMapRenderer()
                        .getMap()
                        .getChunks().size()); // sixth line is the chunks count
        data[6] = String.format("current chunk: %s",
                game
                        .getScreen()
                        .getMapRenderer()
                        .getMap()
                        .getCurrentChunk()
                        .getId());
    }

    public void info() {
        batch.begin();
        for (int i = 0; i < data.length; i++)
            font.draw(batch, data[i], padding, game.getSCREEN_HEIGHT() - (margin * i) - padding);
        batch.end();
    }
}
