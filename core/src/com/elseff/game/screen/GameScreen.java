package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.Player;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private Player player1;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        player1 = new Player(game, this, 300, 400);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255,255,255,1);
        player1.render(game.getBatch(), game.getShapeRenderer(), game.getFont(), delta);
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
