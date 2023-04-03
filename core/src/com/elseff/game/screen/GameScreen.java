package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.FireBall;
import com.elseff.game.model.Player;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private Player player1;
    private OrthographicCamera camera;
    private FireBall fireBall;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        player1 = new Player(game, game.getSCREEN_WIDTH() / 2, game.getSCREEN_HEIGHT() / 2);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.getSCREEN_WIDTH(), game.getSCREEN_HEIGHT());
        fireBall = new FireBall(game, 100, 100, 5f, true, false);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255, 255, 255, 1);
        player1.render(game.getBatch(), game.getFont(), delta);
        fireBall.render(game.getBatch(), delta);
        renderDebugMode();
    }

    private void renderDebugMode() {
        if (game.isDebug()) {
            game.getWindowUtil().render(game.getFont());
        }
    }

    public void update(float dt) {
        cameraUpdate();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);
        game.update(dt);
    }

    public void cameraUpdate(){
        camera.position.x = camera.position.x + (player1.getPosition().x - camera.position.x) * .08f;
        camera.position.y = camera.position.y + (player1.getPosition().y - camera.position.y) * .08f;
        camera.update();
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
        player1.dispose();
    }

    @Override
    public void hide() {
    }

}
