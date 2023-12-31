package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.elseff.game.MyGdxGame;

public class TestScreen implements Screen {

    private final MyGdxGame game;

    public TestScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.4f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        int mouseX = Gdx.input.getX();
        int mouseY = Math.abs(Gdx.input.getY() - game.getSCREEN_HEIGHT());
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), String.format("%s:%s", mouseX, mouseY), 100,100);
        game.getBatch().end();
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
