package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.elseff.game.MyGdxGame;

/**
 * Pause screen class
 */
public class PauseScreen implements Screen {
    private final MyGdxGame game;
    private BitmapFont font;
    private final SpriteBatch batch;
    private final Pixmap cursorPixmap;
    private final Cursor cursor;

    public PauseScreen(MyGdxGame game) {
        this.game = game;
        font = game.getGameResources().getFont("arial.ttf", 50);
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();

        cursorPixmap = new Pixmap(Gdx.files.internal("images/menuCursor.png"));
        cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
    }

    @Override
    public void show() {
        Gdx.graphics.setCursor(cursor);
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
        cursor.dispose();
        cursorPixmap.dispose();
    }
}
