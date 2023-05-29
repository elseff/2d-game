package com.elseff.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.elseff.game.misc.GameResources;
import com.elseff.game.misc.MouseController;
import com.elseff.game.screen.GameOverScreen;
import com.elseff.game.screen.GameScreen;
import com.elseff.game.screen.PauseScreen;
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
    private MouseController mouseController;

    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    private boolean debug;
    private boolean isPaused;
    private float time;

    private ShaderProgram redShader;
    private ShaderProgram blurShader;

    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private PauseScreen pauseScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        SCREEN_WIDTH = 1920;
        SCREEN_HEIGHT = 1080;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        gameResources = new GameResources();
        mouseController = new MouseController(this);

        font = gameResources.getFont("arial.ttf", 15);

        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        pauseScreen = new PauseScreen(this);

        redShader = new ShaderProgram(Gdx.files.internal("shaders/red.vert"),
                Gdx.files.internal("shaders/red.frag"));
        blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur.vert"),
                Gdx.files.internal("shaders/blur.frag"));

        windowUtil = new WindowUtil(this, gameScreen);

        reset();

        setScreen(gameOverScreen);
    }

    public void reset() {
        time = 0.0f;
        debug = false;
        isPaused = false;
    }

    public void update(float delta) {
        this.SCREEN_WIDTH = Gdx.graphics.getWidth();
        this.SCREEN_HEIGHT = Gdx.graphics.getHeight();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            if (isPaused) {
                isPaused = false;
                gameScreen.getSnowflakeController().getGenerationTimer().start();
                setScreen(gameScreen);
            } else {
                isPaused = true;
                gameScreen.getSnowflakeController().getGenerationTimer().stop();
                setScreen(pauseScreen);
            }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F12))
            this.debug = !this.debug;

        time += delta;
        mouseController.update(delta);
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

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public GameOverScreen getGameOverScreen() {
        return gameOverScreen;
    }

    public ShaderProgram getRedShader() {
        return redShader;
    }

    public MouseController getMouseController() {
        return mouseController;
    }
}
