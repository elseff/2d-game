package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class GameResources {
    private final TextureAtlas ninjaAtlas;

    public GameResources() {
        ninjaAtlas = new TextureAtlas(Gdx.files.internal("ninja.atlas"));
    }

    public TextureAtlas.AtlasRegion findRegion(String name){
        return ninjaAtlas.findRegion(name);
    }

    public TextureAtlas.AtlasRegion[] findRegions(){
        Array<TextureAtlas.AtlasRegion> regions = ninjaAtlas.getRegions();

        TextureAtlas.AtlasRegion[] result = new TextureAtlas.AtlasRegion[regions.size];
        for (int i = 0; i < regions.size; i++)
            result[i] = regions.get(i);

        return result;
    }
}
