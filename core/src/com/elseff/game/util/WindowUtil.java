package com.elseff.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

/**
 * Some windows utilities
 * such as additional info about the game and grid
 */
public class WindowUtil {
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final int margin = 20; // 20 by default
    private final int padding = 5; // 5 by default
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Array<String> data; // info tab data
    private final ShapeRenderer shapeRenderer;
    private final Color playerHpBarColor;

    public WindowUtil(MyGdxGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.batch = new SpriteBatch(); // own batch

        this.data = new Array<>(); // info with 8 lines
        data.add("DEBUG MODE"); // first line in info tab is title of debug mode
        this.font = new BitmapFont();
        this.font.setColor(Color.GREEN);
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.scale(2, 2, 2);
        playerHpBarColor = new Color(0f, 1f, 0f, 0.5f);
    }

    public void render() {
        update();
        info();
        playerHpBar();
    }

    public void update() {
        data.clear();
        data.add(String.format("fps: %s",
                Gdx.graphics.getFramesPerSecond()));
        data.add(String.format("delta (1/fps): %f",
                Gdx.graphics.getDeltaTime()));
        data.add(String.format("dimension: %s x %s",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()));
        data.add(String.format("time: %.1f s.",
                game
                        .getTime()));
        data.add(String.format("count chunks: %s.",
                gameScreen
                        .getMap()
                        .getChunks().size));
        data.add(String.format("chunk generator size: %sx%s",
                gameScreen
                        .getPlayer()
                        .getChunkGeneratorRectangle().width,
                gameScreen
                        .getPlayer()
                        .getChunkGeneratorRectangle().height));
        data.add(String.format("player speed: %s",
                gameScreen
                        .getPlayer()
                        .getSpeed().x));
        data.add(String.format("current chunk: %s",
                gameScreen
                        .getMap()
                        .getCurrentChunk()
                        .getId()));
        data.add(String.format("monsters count: %s",
                gameScreen
                        .getMap()
                        .getEnemies().size));
    }

    public void info() {
        batch.begin();
        for (int i = 0; i < data.size; i++)
            font.draw(batch, data.get(i), padding, game.getSCREEN_HEIGHT() - (margin * i) - padding);
        batch.end();
    }

    public void playerHpBar() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
        shapeRenderer.rect(10, 10, 104, 32);
        shapeRenderer.setColor(playerHpBarColor);
        shapeRenderer.rect(12, 12, gameScreen.getPlayer().getHp(), 28);
        shapeRenderer.end();
        if (game.isDebug()) {
            batch.begin();
            font.setColor(Color.WHITE);
            font.draw(batch, String.valueOf((int) gameScreen.getPlayer().getHp()), 110, 55);
            batch.end();
        }
    }
}
