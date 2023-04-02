package com.elseff.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.elseff.game.misc.GameResources;
import com.elseff.game.screen.GameScreen;
import com.elseff.game.util.WindowUtil;

/**
 * Main game class
 */
public class MyGdxGame extends Game {
    private ShapeRenderer shapeRenderer;
    private WindowUtil windowUtil;
    private SpriteBatch batch;
    private BitmapFont font;
    private GameResources gameResources;
    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    private boolean debug = false;
    private float time;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.SCREEN_WIDTH = Gdx.graphics.getWidth();
        this.SCREEN_HEIGHT = Gdx.graphics.getHeight();
        this.shapeRenderer = new ShapeRenderer();
        this.windowUtil = new WindowUtil(this);
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.font = new BitmapFont();
        this.font.setColor(Color.GREEN);
        this.time = 0.0f;
        this.gameResources = new GameResources();
        this.setScreen(new GameScreen(this));
    }

    public void update(float dt) {
        this.SCREEN_WIDTH = Gdx.graphics.getWidth();
        this.SCREEN_HEIGHT = Gdx.graphics.getHeight();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F12))
            this.debug = !this.debug;

        time += dt;
        windowUtil.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.SCREEN_WIDTH = Gdx.graphics.getWidth();
        this.SCREEN_HEIGHT = Gdx.graphics.getHeight();
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public WindowUtil getWindowUtil() {
        return windowUtil;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public float getTime() {
        return time;
    }

    public boolean isDebug() {
        return debug;
    }

    public BitmapFont getFont() {
        return font;
    }

    public GameResources getGameResources() {
        return this.gameResources;
    }
}
