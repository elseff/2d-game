package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.elseff.game.log.LogTag;

public abstract class AbstractScreen implements Screen {

    @Override
    public void show() {
        Gdx.app.log(LogTag.SCREEN.name(), "Switched screen to " + getClass().getSimpleName());
    }
}
