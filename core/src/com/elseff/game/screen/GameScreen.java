package com.elseff.game.screen;

import com.badlogic.gdx.Screen;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.FireBall;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private FireBall fireBall;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.fireBall = new FireBall(100, 100, 5f, true, true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        game.getBatch().begin();
        fireBall.render(game.getBatch());
        game.getBatch().end();

        if (game.isDebug()) {
            game.getWindowUtil().info(game.getBatch(), game.getFont());
            game.getWindowUtil().grid(game.getShapeRenderer());
        }
    }

    public void update(float dt) {
        game.update(dt);
        fireBall.update(dt);
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
        fireBall.dispose();
    }

    @Override
    public void hide() {
    }

}
