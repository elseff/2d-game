package com.elseff.game.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Snowflake {
    private final Rectangle rectangle;
    private final Vector2 direction;
    private final float speed;
    private Color color;
    private float lifetime;

    public Snowflake(float x, float y) {
        int size = (int) (Math.random() * 5 + 3);
        rectangle = new Rectangle(x, y, size, size);
        direction = new Vector2();
        direction.x = (float) Math.random() + 1;
        direction.y = -1;
        speed = (float) Math.random()*2 + 2;
        lifetime = (float) (Math.random() * 5 + 2);
        color = new Color();
    }

    public Snowflake(Vector2 position){
        this(position.x, position.y);
    }

    public void update(float delta) {
        move(delta);
        updateColor();
        lifetime -= delta;
    }

    private void updateColor() {
        //to lower transparency
        color.set(1, 1, 1, lifetime / 7);
    }

    private void move(float delta) {
        this.rectangle.x += direction.x * speed;
        this.rectangle.y += direction.y * speed;
    }

    public float getLifetime() {
        return lifetime;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Color getColor() {
        return color;
    }
}
