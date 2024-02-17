package com.elseff.game.model.player;

import com.badlogic.gdx.math.Vector2;

public class PlayerShadowParticle {
    private final Vector2 position;
    private float lifetime;
    private float opacity;

    public PlayerShadowParticle(Vector2 position, Float lifetime, Float opacity) {
        this.position = position;
        this.lifetime = lifetime;
        this.opacity = opacity;
    }

    public void update(){

    }

    public Vector2 getPosition() {
        return position;
    }

    public float getLifetime() {
        return lifetime;
    }

    public float getOpacity() {
        return opacity;
    }
}
