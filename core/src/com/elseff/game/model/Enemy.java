package com.elseff.game.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public abstract class Enemy extends GameObject {
    private TextureRegion textureRegion;
    private final float SCALE;

    protected Enemy(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
        SCALE = 1;
    }

    public abstract TextureRegion getTexture();

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - textureRegion.getRegionWidth() * SCALE / 2f,
                getPosition().y - textureRegion.getRegionHeight() * SCALE / 2f,
                textureRegion.getRegionWidth() * SCALE,
                textureRegion.getRegionHeight() * SCALE);
    }
}
