package com.elseff.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public abstract class Enemy extends GameObject {
    private TextureRegion textureRegion;
    private final SpriteBatch batch;
    private float hp;

    protected Enemy(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
        hp = 100;
        batch = game.getBatch();
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - textureRegion.getRegionWidth() * getSCALE() / 2f,
                getPosition().y - textureRegion.getRegionHeight() * getSCALE() / 2f,
                textureRegion.getRegionWidth() * getSCALE(),
                textureRegion.getRegionHeight() * getSCALE());
    }

    public void render(float delta){
        batch.draw(getTexture(),
                getPosition().x - getTexture().getRegionWidth() / 2f,
                getPosition().y - getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth() / 2f,
                getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth(),
                getTexture().getRegionHeight(),
                getSCALE(),
                getSCALE(),
                0.0f);
        super.render(delta);
    }

    public abstract void hit(float damage);

    public abstract TextureRegion getTexture();

    public abstract float getSCALE();

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }
}
