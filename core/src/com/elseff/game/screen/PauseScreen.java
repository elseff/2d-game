package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.SnowflakeController;

public class PauseScreen implements Screen {
    private final MyGdxGame game;
    private BitmapFont font;
    private SpriteBatch batch;

    public PauseScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        font = game.getGameResources().getFont("arial.ttf", 50);
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font = game.getGameResources().updateFontSize(font, 50);
        font.draw(batch, "Pause", game.getSCREEN_WIDTH() / 2f - 50, game.getSCREEN_HEIGHT() / 1.25f);
        font = game.getGameResources().updateFontSize(font, 30);
        font.draw(batch, "Press ESC to continue", game.getSCREEN_WIDTH() / 2f - 120, game.getSCREEN_HEIGHT() / 4f);
        batch.end();
    }

    private void update(float delta) {
        game.update(delta);
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
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
