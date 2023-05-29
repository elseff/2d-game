package com.elseff.game.misc.popupmsg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class PopUpMessage {
    private final String text;
    private final Vector2 position;
    private final Color color;
    private float life;
    private boolean isAlive;
    private final PopUpMessageType type;

    public PopUpMessage(String text, Vector2 pos, Color color, PopUpMessageType type) {
        this.text = text;
        this.color = color;
        this.type = type;

        isAlive = true;
        position = new Vector2(pos);
        life = 100;
    }

    public PopUpMessage(String text, float x, float y, Color color,PopUpMessageType type) {
        this(text, new Vector2(x, y), color, type);
    }

    public PopUpMessage(String text, Vector2 pos,PopUpMessageType type) {
        this(text, pos, Color.WHITE, type);
    }

    public PopUpMessage(String text, float x, float y,PopUpMessageType type) {
        this(text, x, y, Color.WHITE, type);
    }

    public void subtractLife(float damage) {
        float temp = life - damage;
        if (temp <= 0)
            isAlive = false;

        life = temp;
    }

    public String getText() {
        return text;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public float getLife() {
        return life;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public PopUpMessageType getType() {
        return type;
    }
}
