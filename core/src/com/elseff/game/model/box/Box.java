package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.GameObject;
import com.elseff.game.screen.GameScreen;

public abstract class Box extends GameObject {
    private final TextureRegion texture;
    private final SpriteBatch batch;
    private float SCALE;

    protected Box(MyGdxGame game, GameScreen gameScreen,float x, float y) {
        super(game, x, y, gameScreen);
        this.texture = getTexture();
        this.batch = getGame().getBatch();
        this.SCALE = 2.0f;
        getRectColor().set(1,1,1,0.5f);
    }

    protected Box(MyGdxGame game,GameScreen gameScreen, Vector2 position){
        this(game, gameScreen, position.x, position.y);
    }

    public abstract TextureRegion getTexture();

    @Override
    public Rectangle getRectangle() {
            return new Rectangle(getPosition().x - getTexture().getRegionWidth() * getSCALE() / 2f,
                    getPosition().y - getTexture().getRegionHeight() * getSCALE() / 2f,
                    getTexture().getRegionWidth() * getSCALE(),
                    getTexture().getRegionHeight() * getSCALE());
    }

    public void render(float delta) {
        batch.draw(texture,
                getPosition().x - texture.getRegionWidth() / 2f,
                getPosition().y - texture.getRegionHeight() / 2f,
                texture.getRegionWidth() / 2f,
                texture.getRegionHeight() / 2f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                SCALE,
                SCALE,
                0.0f);
        super.render(delta);
    }

    public float getSCALE() {
        return SCALE;
    }

    public void setSCALE(float SCALE) {
        this.SCALE = SCALE;
    }
}
