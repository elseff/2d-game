package com.elseff.game.model.food;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.GameObject;
import com.elseff.game.screen.GameScreen;

public abstract class Food extends GameObject {
    private final TextureRegion texture;
    private final SpriteBatch batch;
    private final float SCALE;

    protected Food(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
        batch = game.getBatch();
        texture = getTexture();
        SCALE = 2.0f;

        getRectColor().set(0.1f, 1.0f, 0.1f, 0.5f);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - getTexture().getRegionWidth() * SCALE / 2f,
                getPosition().y - getTexture().getRegionHeight() * SCALE / 2f,
                getTexture().getRegionWidth() * SCALE,
                getTexture().getRegionHeight() * SCALE);
    }

    public void render(float delta) {
        batch.draw(texture,
                getPosition().x - texture.getRegionWidth() / 2f,
                getPosition().y - texture.getRegionWidth() / 2f,
                texture.getRegionWidth() / 2f,
                texture.getRegionHeight() / 2f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                SCALE,
                SCALE,
                0.0f);
        super.render(delta);
    }

    public abstract TextureRegion getTexture();
}
