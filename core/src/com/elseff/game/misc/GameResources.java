package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.elseff.game.misc.font.FontDefinition;

import java.util.HashMap;
import java.util.Map;

public class GameResources {
    private final TextureAtlas atlas;
    private final Map<FontDefinition, BitmapFont> fonts;

    public GameResources() {
        String name = "textures.atlas";
        atlas = new TextureAtlas(Gdx.files.internal(name));
        Gdx.app.log("RESOURCE", "loaded - " + name);
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
        Gdx.app.log("FONT", "Generated font - " + definition);
        return font;
    }
}
