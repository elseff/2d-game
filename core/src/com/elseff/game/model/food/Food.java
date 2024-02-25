package com.elseff.game.model.food;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.GameObject;
import com.elseff.game.screen.GameScreen;

public abstract class Food extends GameObject {
    private final SpriteBatch batch;
    private final float SCALE;
    private final Color rectColor;
    private final Rectangle rectangle;

    protected Food(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
        batch = game.getBatch();
        SCALE = 2.0f;
        rectColor = new Color(0.1f, 1.0f, 0.1f, 0.3f);
        getRectColor().set(rectColor);
        rectangle = new Rectangle();
    }

    @Override
    public Rectangle getRectangle() {
        rectangle.set(getPosition().x - getTexture().getRegionWidth() * SCALE / 2f,
                getPosition().y - getTexture().getRegionHeight() * SCALE / 2f,
                getTexture().getRegionWidth() * SCALE,
                getTexture().getRegionHeight() * SCALE);

        return rectangle;
    }

    public void render(float delta) {
        batch.draw(getTexture(),
                getPosition().x - getTexture().getRegionWidth() / 2f,
                getPosition().y - getTexture().getRegionWidth() / 2f,
                getTexture().getRegionWidth() / 2f,
                getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth(),
                getTexture().getRegionHeight(),
                SCALE,
                SCALE,
                0.0f);
        super.render(delta);
    }

    public abstract TextureRegion getTexture();
}
