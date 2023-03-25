package com.elseff.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.elseff.game.misc.FireBall;
import com.elseff.game.util.MyInputProcessor;
import com.elseff.game.util.WindowUtil;

public class MyGdxGame extends Game {
    private ShapeRenderer shapeRenderer;
    private WindowUtil windowUtil;
    private FireBall fireBall;
    private SpriteBatch batch;
    private BitmapFont font;
    private MyInputProcessor myInputProcessor;

    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    private boolean DEBUG_MODE = true;
    private float time;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.SCREEN_WIDTH = Gdx.graphics.getWidth();
        this.SCREEN_HEIGHT = Gdx.graphics.getHeight();
        this.fireBall = new FireBall(100, 100, 5f, 0, true, true);
        this.shapeRenderer = new ShapeRenderer();
        this.windowUtil = new WindowUtil(this);
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.font = new BitmapFont();
        this.font.setColor(Color.GREEN);
        this.time = 0.0f;
        this.myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(Color.DARK_GRAY);
        batch.begin();
        fireBall.render(batch);
        batch.end();

        if (!DEBUG_MODE) return;
        windowUtil.info(batch, font);
        windowUtil.grid(shapeRenderer);
    }

    @Override
    public void dispose() {
        batch.dispose();
        fireBall.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }

    public void update(float dt) {
        if (myInputProcessor.keyDown(Input.Keys.ESCAPE))
            Gdx.app.exit();
        if (myInputProcessor.keyDown(Input.Keys.F12))
            DEBUG_MODE = !DEBUG_MODE;

        time += dt;
        fireBall.update(dt);
        windowUtil.update();
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public float getTime() {
        return time;
    }
}
