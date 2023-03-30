package com.elseff.game.misc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FireBall {
    private final TextureRegion texture;
    private float movementForceCounter;
    private int movementCounter;
    private final float movementForce;
    private final Vector2 position;
    private float angle;
    private final float SCALE;
    private final float rotationSpeed;
    private final boolean rotating;
    private final boolean moving;

    public FireBall(float x, float y, float SCALE, boolean rotating, boolean moving) {
        this.texture = new TextureRegion(new Texture("fireball.png"));
        this.position = new Vector2(x, y);
        this.movementForce = SCALE * 40f;
        this.movementForceCounter = movementForce;
        this.movementCounter = 0; // for waiting before start moving
        this.angle = 0.0f;
        this.SCALE = SCALE;
        this.rotationSpeed = 2.5f; // 0-4
        this.rotating = rotating;
        this.moving = moving;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture,
                position.x - texture.getRegionWidth() / 2f,
                position.y - texture.getRegionWidth() / 2f,
                texture.getRegionWidth() / 2f,
                texture.getRegionHeight() / 2f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                SCALE,
                SCALE,
                angle);
    }

    public void update(float dt) {
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

            this.position.y += Math.abs(movementForceCounter) * dt;
        } else if (movementCounter > 50)
            this.position.y -= Math.abs(movementForceCounter) * dt;

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
