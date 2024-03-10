package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.misc.Snowflake;
import com.elseff.game.misc.SnowflakeController;
import com.elseff.game.misc.popupmsg.PopUpMessagesController;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.box.Box;
import com.elseff.game.model.box.SmallCardBox;
import com.elseff.game.model.enemy.Enemy;
import com.elseff.game.model.player.Player;
import com.elseff.game.util.MathUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main game screen class
 */
public class GameScreen extends AbstractScreen {
    private MyGdxGame game;
    private Player player;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Map map;
    private SnowflakeController snowflakeController;
    private PopUpMessagesController popUpMessagesController;
    private Vector2 tempVector;
    private Music oselSound;
    private SimpleDateFormat simpleDateFormat;
    private Date date;

    public GameScreen(MyGdxGame game) {
        init(game);
    }

    public void init(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera(100, 100);
        viewport = new ScreenViewport(camera);

        player = new Player(game, this, 0, 0);

        map = new Map(game, this);

        //on the center of the start chunk
        Chunk initialChunk = map.getCurrentChunks().iterator().next();
        player.getPosition().set(initialChunk.getPosition().x + initialChunk.getWidthPixels() / 2f,
                initialChunk.getPosition().y + initialChunk.getHeightPixels() / 2f);

        camera.setToOrtho(
                false,
                game.getSCREEN_WIDTH(),
                game.getSCREEN_HEIGHT());

        game
                .getFont()
                .getData()
                .setScale(0.75f, 0.75f);

        camera.zoom = 1f;
        camera.position.set(player.getPosition(), 0);

        snowflakeController = new SnowflakeController(game, this);
        popUpMessagesController = new PopUpMessagesController(game);

        tempVector = new Vector2();

        oselSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/osel.mp3"));

        simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss:SSS");
        date = new Date();
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);
        snowflakeController.start();
        super.show();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.01f, 0.4f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.BATCH_BEGIN();
        map.render(delta);

        player.render(delta);

        renderDebugMode();

        snowflakeController.render(delta);
        game.getWindowUtil().playerHpBar();
        game.getWindowUtil().renderMouse();
        popUpMessagesController.render(delta);
        game.BATCH_END();
    }

    private void renderDebugMode() {
        if (!game.isDebug()) return;

        game.getWindowUtil().info();
    }

    private void update(float delta) {
        game.update(delta);
        checkGameOver();
        cameraUpdate(delta);
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getShapeRenderer().setProjectionMatrix(camera.combined);
        checkPutBox(delta);
        map.update();
        playerSpeedUpdate(delta);
        killPlayer();
    }

    private void cameraUpdate(float delta) {
        cameraZoomUpdate(delta);
        camera.position.x = camera.position.x + (player.getPosition().x - camera.position.x) * delta * 5;
        camera.position.y = camera.position.y + (player.getPosition().y - camera.position.y) * delta * 5;
        camera.update();
        player.getChunkGeneratorRectangle().x = camera.position.x - player.getChunkGeneratorRectangle().width / 2;
        player.getChunkGeneratorRectangle().y = camera.position.y - player.getChunkGeneratorRectangle().height / 2;
    }

    private Date now() {
        date.setTime(TimeUtils.millis());
        return date;
    }

    public void checkPutBox(float delta) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 mousePos = game.getMouseController().getWorldMousePosition();
            Box box = new SmallCardBox(game, this, 0, 0);
            map.getCurrentChunks().forEach(currentChunk -> {
                        tempVector.set(
                                (float) (Math.floor(currentChunk.getPosition().x + (int) ((mousePos.x - currentChunk.getRectangle().x) / 32) * 32f))
                                        + box.getRectangle().width / 2f,
                                (float) (Math.floor(currentChunk.getPosition().y + (int) ((mousePos.y - currentChunk.getRectangle().y) / 32) * 32f))
                                        + box.getRectangle().height / 2f);
                        box.getPosition().set(tempVector);
                        if (map.isAreaClear(box.getRectangle())) {
                            currentChunk.addGameObject(box);
                            Gdx.app.log(CURRENT_TIME(), "added box to " + box.getPosition());
                        }
                    }
            );

        } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2 mousePos = game.getMouseController().getWorldMousePosition();
            map.getCurrentChunks().forEach(currentChunk -> {
                for (int j = 0; j < currentChunk.getObjects().size; j++) {
                    GameObject gameObject = currentChunk.getObjects().get(j);
                    if (gameObject.getRectangle().contains(mousePos)) {
                        currentChunk.getObjects().removeValue(gameObject, true);
                        Gdx.app.log(CURRENT_TIME(), "removed box from " + gameObject.getPosition());
                        break;
                    }
                }
            });
        }
    }

    private String CURRENT_TIME() {
        return simpleDateFormat.format(now());
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
            oselSound.play();
            Gdx.app.log(CURRENT_TIME(), "\tGAME OVER!!!");
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

    private void killPlayer() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.K))
            player.hit(101);
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

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    public Array<Snowflake> getParticles() {
        return snowflakeController.getSnowflakes();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SnowflakeController getSnowflakeController() {
        return snowflakeController;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public PopUpMessagesController getPopUpMessagesController() {
        return popUpMessagesController;
    }
}
