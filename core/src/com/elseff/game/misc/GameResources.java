package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.elseff.game.misc.font.FontDefinition;

import java.util.HashMap;

public class GameResources {
    private final TextureAtlas atlas;
    private final HashMap<FontDefinition, BitmapFont> fonts;

    public GameResources() {
        atlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));
        fonts = new HashMap<>();
    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        return atlas.findRegion(name);
    }

    public BitmapFont getFontFromDef(FontDefinition definition) {
        BitmapFont font = fonts.get(definition);
        if (font == null) {
            fonts.put(definition, generateFont(definition));
            font = fonts.get(definition);
        }

        return font;
    }

    private BitmapFont generateFont(FontDefinition definition) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + definition.getFontName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = definition.getFontSize();
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
