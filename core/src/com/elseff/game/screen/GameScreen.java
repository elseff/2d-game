package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.map.MapController;
import com.elseff.game.map.MapRenderer;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.model.Player;

/**
 * Main game screen class
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;
    private Player player;
    private OrthographicCamera camera;
    private MapRenderer mapRenderer;
    private MapController mapController;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        mapRenderer = new MapRenderer(game);
        Map map = mapRenderer.getMap();
        Chunk chunk = map.getChunkById(0);
        player = new Player(
                game,
                chunk.getWidthPixels() / 2f,
                chunk.getHeightPixels() / 2f);
        camera.setToOrtho(
                false,
                game.getSCREEN_WIDTH(),
                game.getSCREEN_HEIGHT());
        game.getFont().getData().setScale(0.75f, 0.75f);
        camera.zoom = 0.5f;
        camera.position.set(player.getPosition(), 0);
        mapController = new MapController(game, map);
        mapController.init();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255, 255, 255, 1);

        game.getBatch().begin();
        mapRenderer.render(delta);
        player.render(delta);
        renderDebugMode();
        game.getBatch().end();
    }

    private void renderDebugMode() {
        if (game.isDebug())
            game.getWindowUtil().render();
    }

    private void update(float dt) {
        cameraUpdate(dt);
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);
        game.update(dt);
        mapController.update();
    }

    private void cameraUpdate(float delta) {
        cameraZoomUpdate(delta);
        camera.position.x = camera.position.x + (player.getPosition().x - camera.position.x) * 0.07f;
        camera.position.y = camera.position.y + (player.getPosition().y - camera.position.y) * 0.07f;
        camera.update();
    }

    private void cameraZoomUpdate(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            if (camera.zoom <= 1)
                camera.zoom += delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) {
            if (camera.zoom >= 0.1)
                camera.zoom -= delta;
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

    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public MapController getMapController() {
        return mapController;
    }
}
