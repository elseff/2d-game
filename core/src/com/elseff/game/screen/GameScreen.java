package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.Body;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private Body body1;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        body1 = new Body(game, 300, 400, Color.WHITE);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        body1.render(game.getBatch(), game.getShapeRenderer(), delta);
        checkDebugMode();
    }

    private void checkDebugMode() {
        if (game.isDebug()) {
            game.getWindowUtil().info(game.getBatch(), game.getFont());
            game.getWindowUtil().grid(game.getShapeRenderer());
        }
    }

    public void update(float dt) {
        game.update(dt);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
    }

}
