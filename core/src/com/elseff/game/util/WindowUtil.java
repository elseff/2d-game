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
    private final Color gridColor = Color.GRAY;
    private final MyGdxGame game;
    private final int margin = 20; // 20 by default
    private final int padding = 5; // 5 by default
    private final SpriteBatch batch;
    private final String[] data; // info tab data

    public WindowUtil(MyGdxGame game) {
        this.game = game;
        this.batch = new SpriteBatch(); // own batch

        this.data = new String[5]; // info with 5 lines
        this.data[0] = "DEBUG MODE"; // first line in info tab is title of debug mode
    }

//    public void grid() {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        for (int i = 0; i <= game.getViewport().getScreenWidth(); i += game.getSCREEN_WIDTH() / (Gdx.graphics.getWidth() / 100f))
//            shapeRenderer.line(i, game.getSCREEN_HEIGHT(), i, 0, gridColor, gridColor);
//        for (int i = 0; i <= game.getSCREEN_HEIGHT(); i += game.getSCREEN_HEIGHT() / (Gdx.graphics.getHeight() / 100f))
//            shapeRenderer.line(0, i, game.getSCREEN_WIDTH(), i, gridColor, gridColor);
//        shapeRenderer.end();
//    }

    public void render(BitmapFont font) {
        update();
        info(font);
    }

    public void update() {
        data[1] = String.format("fps: %s", Gdx.graphics.getFramesPerSecond()); // second line if spf
        data[2] = String.format("delta (1/fps): %f", Gdx.graphics.getDeltaTime()); // third line is current delta time
        data[3] = String.format("dimension: %s x %s", Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // fourth line is current screen dimension
        data[4] = String.format("time: %.1f s.", game.getTime()); // fifth line is time since game starting
    }

    public void info(BitmapFont font) {
        batch.begin();
        for (int i = 0; i < data.length; i++)
            font.draw(batch, data[i], padding, game.getSCREEN_HEIGHT() - (margin * i) - padding);
        batch.end();
    }
}
