package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.misc.Snowflake;
import com.elseff.game.misc.SnowflakeController;
import com.elseff.game.model.Player;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private Player player;
    private OrthographicCamera camera;
    private Map map;
    private SnowflakeController snowflakeController;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        player = new Player(
                game,
                this,
                0, 0);

        map = new Map(game, this);

        //on the center of the start chunk
        player.getPosition().set(map.getCurrentChunk().getPosition().x + map.getCurrentChunk().getWidthPixels() / 2f,
                map.getCurrentChunk().getPosition().y + map.getCurrentChunk().getHeightPixels() / 2f);

        camera.setToOrtho(
                false,
                game.getSCREEN_WIDTH(),
                game.getSCREEN_HEIGHT());

        game
                .getFont()
                .getData()
                .setScale(0.75f, 0.75f);

        camera.zoom = 0.5f;
        camera.position.set(player.getPosition(), 0);


        snowflakeController = new SnowflakeController(game, this);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.01f, 0.4f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        game.getBatch().begin();
        map.render(delta);
        player.render(delta);
        renderDebugMode();
        game.getBatch().end();
        snowflakeController.render(delta);
        game.getWindowUtil().playerHpBar();
    }

    private void renderDebugMode() {
        if (game.isDebug()) {
            game.getWindowUtil().info();
        }
    }

    private void update(float delta) {
        checkGameOver();
        cameraUpdate(delta);
        game.getWindowUtil().update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);
        game.update(delta);
        map.update();
        playerSpeedUpdate(delta);
    }

    private void cameraUpdate(float delta) {
        cameraZoomUpdate(delta);
        camera.position.x = camera.position.x + (player.getPosition().x - camera.position.x) * delta * 5;
        camera.position.y = camera.position.y + (player.getPosition().y - camera.position.y) * delta * 5;
        camera.update();
        player.getChunkGeneratorRectangle().x = camera.position.x - player.getChunkGeneratorRectangle().width / 2;
        player.getChunkGeneratorRectangle().y = camera.position.y - player.getChunkGeneratorRectangle().height / 2;
    }

    private void cameraZoomUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            if (camera.zoom <= 4)
                camera.zoom += delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) {
            if (camera.zoom > 0.5)
                camera.zoom -= delta;
        }
    }

    private void checkGameOver() {
        if (player.getHp() < 0) {
            game.setScreen(game.getGameOverScreen());
        }
    }

    private void playerSpeedUpdate(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
            player.changeSpeed(100);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (player.getSpeed().x >= 100)
                player.changeSpeed(-100);
        }

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

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    public Array<Snowflake> getParticles() {
        return snowflakeController.getParticles();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
