package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameResources {
    private final TextureAtlas atlas;

    public GameResources() {
        atlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));
    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        return atlas.findRegion(name);
    }

}
