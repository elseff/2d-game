package com.elseff.game.map.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class TriggerRegion {
    private static final Color color = new Color(255, 102, 0, 0.5f);
    private final float width;
    private final Rectangle rectangle;
    private final TriggerPosition triggerPosition;
    private final Chunk chunk;

    public TriggerRegion(TriggerPosition triggerPosition, Chunk chunk) {
        this.triggerPosition = triggerPosition;
        this.chunk = chunk;
        this.rectangle = new Rectangle();
        this.width = 16;
        initRectangle();
    }

    public void initRectangle() {
        switch (triggerPosition) {
            case LEFT -> rectangle.set(
                    chunk.getRectangle().x,
                    chunk.getRectangle().y,
                    width,
                    chunk.getRectangle().height
            );
            case RIGHT -> rectangle.set(
                    chunk.getRectangle().x + chunk.getRectangle().width - width,
                    chunk.getRectangle().y,
                    width,
                    chunk.getRectangle().height
            );
            case TOP -> rectangle.set(
                    chunk.getRectangle().x,
                    chunk.getRectangle().y + chunk.getRectangle().height - width,
                    chunk.getRectangle().width,
                    width);
            case BOTTOM -> rectangle.set(
                    chunk.getRectangle().x,
                    chunk.getRectangle().y,
                    chunk.getRectangle().width,
                    width
            );
        }
    }

    public static Color getColor() {
        return color;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public TriggerPosition getTriggerPosition() {
        return triggerPosition;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public String toString() {
        return "TriggerRegion{" +
                "rectangle=" + rectangle +
                ", triggerPosition=" + triggerPosition +
                '}';
    }
}
