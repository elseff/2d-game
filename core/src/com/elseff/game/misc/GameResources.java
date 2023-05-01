package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameResources {
    private final TextureAtlas atlas;

    public GameResources() {
        atlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));
    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        return atlas.findRegion(name);
    }

    public BitmapFont getFont(String name, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + name));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    public BitmapFont updateFontSize(BitmapFont font, int size) {
        String name = font.getData().name.split("-")[0] + ".ttf";
        return getFont(name, size);
    }
}
