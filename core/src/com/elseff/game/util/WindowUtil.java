package com.elseff.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.font.FontDefinition;
import com.elseff.game.model.player.Player;
import com.elseff.game.screen.GameScreen;

/**
 * Some windows utilities
 * such as additional info about the game and grid
 */
public class WindowUtil {
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final int margin = 20; // 20 by default
    private final int padding = 5; // 5 by default
    private final SpriteBatch ownBatch;
    private final BitmapFont font;
    private final Array<String> data; // info tab data
    private final ShapeRenderer ownShapeRenderer;
    private final Color playerHpBarColor;
    private final float playerHpBarColorOpacity;

    public WindowUtil(MyGdxGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.ownBatch = new SpriteBatch(); // own batch

        this.data = new Array<>();
        data.add("DEBUG MODE"); // first line in info tab is title of debug mode
        this.font = new BitmapFont();
        this.font.setColor(Color.GREEN);
        this.ownShapeRenderer = new ShapeRenderer();
        ownShapeRenderer.scale(2, 2, 2);
        playerHpBarColorOpacity = 0.65f;
        playerHpBarColor = new Color(0f, 1f, 0f, playerHpBarColorOpacity);
    }

    public void render() {
        update();
        info();
        playerHpBar();
    }

    public void update() {
        updateHpBarColor();

        data.clear();
        data.add(String.format("fps: %s",
                Gdx.graphics.getFramesPerSecond()));
        data.add(String.format("delta (1/fps): %f",
                Gdx.graphics.getDeltaTime()));
        data.add(String.format("dimension: %s x %s",
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()));
        data.add(String.format("time: %.1f s.",
                game
                        .getTime()));
        data.add(String.format("camera.zoom: %.1f",
                gameScreen
                        .getCamera().zoom));
        data.add(String.format("count chunks: %s.",
                gameScreen
                        .getMap()
                        .getChunks().size));
        data.add(String.format("chunk generator size: %sx%s",
                gameScreen
                        .getPlayer()
                        .getChunkGeneratorRectangle().width,
                gameScreen
                        .getPlayer()
                        .getChunkGeneratorRectangle().height));
        data.add(String.format("player speed: %s",
                gameScreen
                        .getPlayer()
                        .getSpeed().x));
        data.add(String.format("monsters count: %s",
                gameScreen
                        .getMap()
                        .getEnemies().size));
        data.add(String.format("snowflakes count: %s",
                gameScreen
                        .getParticles()
                        .size));
        data.add(String.format("mouse pos: (%s;%s)",
                game
                        .getMouseController().getMouseX(),
                game
                        .getMouseController().getMouseY()));
        data.add(String.format("world mouse pos: (%s;%s)",
                game
                        .getMouseController().getWorldMouseX(),
                game
                        .getMouseController().getWorldMouseY()));
        data.add(String.format("player shadow particles count: %s",
                game.getGameScreen().getPlayer().getParticlesPositions().size()));
    }

    private void updateHpBarColor() {
        Player player = gameScreen.getPlayer();
        if (player.getHp() >= 70) {
            playerHpBarColor.set(0f, 1f, 0f, playerHpBarColorOpacity);
        } else if (player.getHp() >= 30)
            playerHpBarColor.set(1, 1, 0, playerHpBarColorOpacity);
        else
            playerHpBarColor.set(1, 0, 0, playerHpBarColorOpacity);
    }

    public void info() {
        ownBatch.begin();
        for (int i = 0; i < data.size; i++) {
            font.setColor(Color.WHITE);
            font.draw(ownBatch, data.get(i), padding, game.getSCREEN_HEIGHT() - (margin * i) - padding);
        }
        ownBatch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
    }

    public void playerHpBar() {
        ownBatch.begin();
        BitmapFont arial30 = game.getGameResources().getFontFromDef(FontDefinition.ARIAL_30);
        arial30.setColor(Color.GOLDENROD);
        arial30.draw(ownBatch, "Your hp", 30, 110);
        ownBatch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        ownShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        ownShapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
        ownShapeRenderer.rect(10, 10, 104, 32);
        ownShapeRenderer.setColor(playerHpBarColor);
        ownShapeRenderer.rect(12, 12, gameScreen.getPlayer().getHp(), 28);
        ownShapeRenderer.end();

        // count of hp text
        if (game.isDebug()) {
            ownBatch.begin();
            this.font.setColor(Color.WHITE);
            this.font.draw(ownBatch, String.valueOf((int) gameScreen.getPlayer().getHp()), 110, 55);
            ownBatch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
        }
    }

    public void renderMouse() {
        game.GRACEFUL_SHAPE_RENDERER_BEGIN(ShapeRenderer.ShapeType.Filled);
        game.getShapeRenderer().setColor(Color.WHITE);
        Vector2 mousePos = game.getMouseController().getWorldMousePosition();
        game.getShapeRenderer().circle(mousePos.x, mousePos.y, 5);
        game.GRACEFUL_SHAPE_RENDERER_END();
    }
}
