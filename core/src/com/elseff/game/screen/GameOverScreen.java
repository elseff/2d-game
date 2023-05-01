package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.elseff.game.MyGdxGame;

public class GameOverScreen implements Screen {
    private final MyGdxGame game;
    private BitmapFont font;
    private final SpriteBatch batch;

    public GameOverScreen(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = game.getGameResources().getFont("arial.ttf", 50);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font = game.getGameResources().updateFontSize(font, 50);
        font.draw(batch, "GAME OVER", game.getSCREEN_WIDTH() / 2f - 80, game.getSCREEN_HEIGHT() / 1.25f);
        font = game.getGameResources().updateFontSize(font, 30);
        font.draw(batch, "Press ESC to exit", game.getSCREEN_WIDTH() / 2f - 100, game.getSCREEN_HEIGHT() / 4f);
        font.draw(batch, "Press SPACE to restart", game.getSCREEN_WIDTH() / 2f - 120, game.getSCREEN_HEIGHT() / 5f);
        batch.end();
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.reset();
            game.getGameScreen().init(game);
            game.setScreen(game.getGameScreen());
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
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
