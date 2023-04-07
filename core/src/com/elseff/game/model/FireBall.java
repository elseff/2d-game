package com.elseff.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.MyGdxGame;

/**
 * May can be used for downloading screens
 */
public class FireBall extends GameObject {
    private final TextureRegion texture;
    private final SpriteBatch batch;
    private float movementForceCounter;
    private int movementCounter;
    private final float movementForce;
    private float angle;
    private final float SCALE;
    private final float rotationSpeed;
    private final boolean rotating;
    private final boolean moving;

    public FireBall(MyGdxGame game, float x, float y, float SCALE, boolean rotating, boolean moving) {
        super(game, x, y);
        this.texture = getGame().getGameResources().findRegion("fireball");
        this.movementForce = SCALE * 40f;
        this.movementForceCounter = movementForce;
        this.movementCounter = 0; // for waiting before start moving
        this.angle = 0.0f;
        this.SCALE = SCALE;
        this.rotationSpeed = 2.5f; // 0-4
        this.rotating = rotating;
        this.moving = moving;
        this.batch = game.getBatch();
    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        batch.draw(texture,
                getPosition().x - texture.getRegionWidth() / 2f,
                getPosition().y - texture.getRegionWidth() / 2f,
                texture.getRegionWidth() / 2f,
                texture.getRegionHeight() / 2f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                SCALE,
                SCALE,
                angle);
        batch.end();
    }


    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - texture.getRegionWidth() * SCALE / 2f,
                getPosition().y - texture.getRegionHeight() * SCALE / 2f,
                texture.getRegionWidth() * SCALE,
                texture.getRegionHeight() * SCALE);
    }

    private void update(float dt) {
        if (rotating)
            rotate(dt);
        if (moving)
            move(dt);
    }

    private void move(float dt) {
        if (movementCounter > 100)
            movementCounter = 0;

        if (movementCounter >= 0 && movementCounter < 50) {
            if (movementCounter == 0) // for first step
                movementForceCounter = movementForce; // set to default

            getPosition().y += Math.abs(movementForceCounter) * dt;
        } else if (movementCounter > 50)
            getPosition().y -= Math.abs(movementForceCounter) * dt;

        movementForceCounter -= movementForce / 50;
        movementCounter++;
    }

    private void rotate(float dt) {
        if (this.angle >= 360)
            this.angle = 0;

        this.angle += rotationSpeed * 90 * dt;
    }

    public void dispose() {
        this.texture.getTexture().dispose();
    }
}
