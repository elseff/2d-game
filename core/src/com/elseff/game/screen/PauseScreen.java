package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.font.FontDefinition;

/**
 * Pause screen class
 */
public class PauseScreen extends AbstractScreen {
    private final MyGdxGame game;
    private BitmapFont font;
    private final SpriteBatch batch;
    private final Pixmap cursorPixmap;
    private final Cursor cursor;

    public PauseScreen(MyGdxGame game) {
        this.game = game;
        font = game.getGameResources().getFontFromDef(FontDefinition.ARIAL_50);
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();

        cursorPixmap = new Pixmap(Gdx.files.internal("images/menuCursor.png"));
        cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
    }

    @Override
    public void show() {
        Gdx.graphics.setCursor(cursor);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setCursorPosition(game.getSCREEN_WIDTH()/2,game.getSCREEN_HEIGHT()/2);
        super.show();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font = game.getGameResources().getFontFromDef(FontDefinition.ARIAL_50);
        font.draw(batch, "Pause", game.getSCREEN_WIDTH() / 2f - 50, game.getSCREEN_HEIGHT() / 1.25f);
        font = game.getGameResources().getFontFromDef(FontDefinition.ARIAL_30);
        font.draw(batch, "Press ESC to continue", game.getSCREEN_WIDTH() / 2f - 120, game.getSCREEN_HEIGHT() / 4f);
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
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
