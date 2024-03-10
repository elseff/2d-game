package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Obstacle;
import com.elseff.game.screen.GameScreen;

public abstract class Box extends GameObject implements Obstacle {
    private final SpriteBatch batch;
    private final float SCALE;
    private final Rectangle rectangle;

    protected Box(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, x, y, gameScreen);
        this.batch = getGame().getBatch();
        this.SCALE = 2.0f;
        getRectColor().set(1, 1, 1, 0.5f);
        rectangle = new Rectangle();
    }

    protected Box(MyGdxGame game, GameScreen gameScreen, Vector2 position) {
        this(game, gameScreen, position.x, position.y);
    }

    public abstract TextureRegion getTexture();

    @Override
    public Rectangle getRectangle() {
        rectangle.set(getPosition().x - getTexture().getRegionWidth() * SCALE / 2f,
                getPosition().y - getTexture().getRegionHeight() * SCALE / 2f,
                getTexture().getRegionWidth() * SCALE,
                getTexture().getRegionHeight() * SCALE);

        return rectangle;
    }

    public void render(float delta) {
        getGame().GRACEFUL_SHAPE_RENDERER_END();
        batch.draw(getTexture(),
                getPosition().x - getTexture().getRegionWidth() / 2f,
                getPosition().y - getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth() / 2f,
                getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth(),
                getTexture().getRegionHeight(),
                SCALE,
                SCALE,
                0.0f);
        getGame().GRACEFUL_SHAPE_RENDERER_BEGIN();
        super.render(delta);
    }
}
