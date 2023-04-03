package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class GameResources {
    private final TextureAtlas atlas;

    public GameResources() {
        atlas = new TextureAtlas(Gdx.files.internal("ninja_fireball.atlas"));
    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        return atlas.findRegion(name);
    }

    public TextureAtlas.AtlasRegion[] findRegions() {
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();

        TextureAtlas.AtlasRegion[] result = new TextureAtlas.AtlasRegion[regions.size];
        for (int i = 0; i < regions.size; i++)
            result[i] = regions.get(i);

        return result;
    }
}
