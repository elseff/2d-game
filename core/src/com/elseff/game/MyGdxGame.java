package com.elseff.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.elseff.game.misc.GameResources;
import com.elseff.game.misc.MouseController;
import com.elseff.game.misc.font.FontDefinition;
import com.elseff.game.screen.GameOverScreen;
import com.elseff.game.screen.GameScreen;
import com.elseff.game.screen.InitialScreen;
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

    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private PauseScreen pauseScreen;

    public MyGdxGame(int width, int height) {
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        gameResources = new GameResources();
        mouseController = new MouseController(this);

        font = gameResources.getFontFromDef(FontDefinition.ARIAL_15);
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        pauseScreen = new PauseScreen(this);

        redShader = new ShaderProgram(Gdx.files.internal("shaders/red.vert"),
                Gdx.files.internal("shaders/red.frag"));

        windowUtil = new WindowUtil(this, gameScreen);

        reset();

        setScreen(new InitialScreen(this));
//        setScreen(gameScreen);
    }

    public void reset() {
        time = 0.0f;
        debug = false;
        isPaused = false;
    }

    public void update(float delta) {
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isPaused) {
                isPaused = false;
                gameScreen.getSnowflakeController().getGenerationTimer().start();
                setScreen(gameScreen);
            } else {
                isPaused = true;
                gameScreen.getSnowflakeController().getGenerationTimer().stop();
                setScreen(pauseScreen);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F12)) {
            debug = !debug;
        }

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
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        System.out.println("resized game");
    }

    public void BATCH_BEGIN() {
        if (batch.isDrawing()) return;
        batch.begin();
    }

    public void BATCH_END() {
        if (!batch.isDrawing()) return;
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
    }

    public void GRACEFUL_SHAPE_RENDERER_BEGIN(ShapeRenderer.ShapeType type) {
        if (shapeRenderer.isDrawing()) {
            shapeRenderer.set(type);
            return;
        }

        BATCH_END();
        shapeRenderer.begin(type);
    }

    public void GRACEFUL_SHAPE_RENDERER_BEGIN() {
        if (shapeRenderer.isDrawing()) return;

        BATCH_END();
        shapeRenderer.begin();
    }

    public void GRACEFUL_SHAPE_RENDERER_END() {
        if (!shapeRenderer.isDrawing()) return;
        shapeRenderer.end();
        BATCH_BEGIN();
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
        if (gameScreen == null)
            gameScreen = new GameScreen(this);

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
