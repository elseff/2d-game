package com.elseff.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    @Override
    public boolean keyDown(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }
}
