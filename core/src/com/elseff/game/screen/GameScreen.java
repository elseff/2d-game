package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.Player;
import com.elseff.game.model.box.SmallCardBox;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private Player player;
    private OrthographicCamera camera;
    private SmallCardBox cardBox;
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        player = new Player(game, 0, 0);
        camera = new OrthographicCamera();
        cardBox = new SmallCardBox(game, 100, 100);
        camera.setToOrtho(false, game.getSCREEN_WIDTH()/1.5f, game.getSCREEN_HEIGHT()/1.5f);
        game.getFont().getData().setScale(0.75f,0.75f);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255, 255, 255, 1);
        player.render(delta);
        cardBox.render(delta);
        renderDebugMode();
    }

    private void renderDebugMode() {
        if (game.isDebug())
            game.getWindowUtil().render();
    }

    public void update(float dt) {
        cameraUpdate();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);
        game.update(dt);
    }

    public void cameraUpdate(){
        camera.position.x = camera.position.x + (player.getPosition().x - camera.position.x) * .07f;
        camera.position.y = camera.position.y + (player.getPosition().y - camera.position.y) * .07f;
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
        player.dispose();
    }

    @Override
    public void hide() {
    }

}
