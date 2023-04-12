package com.elseff.game.map.chunk.trigger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.map.chunk.Chunk;

public class ChunkTrigger {
    private static final Color color = new Color(255, 102, 0, 0.5f);
    private final float width;
    private final Rectangle rectangle;
    private final ChunkTriggerPosition chunkTriggerPosition;
    private final Chunk chunk;

    public ChunkTrigger(ChunkTriggerPosition chunkTriggerPosition, Chunk chunk) {
        this.chunkTriggerPosition = chunkTriggerPosition;
        this.chunk = chunk;
        this.rectangle = new Rectangle();
        this.width = 16;
        initRectangle();
    }

    public void initRectangle() {
        switch (chunkTriggerPosition) {
            case LEFT -> rectangle.set(
                    chunk.getRectangle().x-width,
                    chunk.getRectangle().y,
                    width,
                    chunk.getRectangle().height
            );
            case RIGHT -> rectangle.set(
                    chunk.getRectangle().x + chunk.getRectangle().width,
                    chunk.getRectangle().y,
                    width,
                    chunk.getRectangle().height
            );
            case TOP -> rectangle.set(
                    chunk.getRectangle().x,
                    chunk.getRectangle().y + chunk.getRectangle().height,
                    chunk.getRectangle().width,
                    width);
            case BOTTOM -> rectangle.set(
                    chunk.getRectangle().x,
                    chunk.getRectangle().y-width,
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

    public ChunkTriggerPosition getTriggerPosition() {
        return chunkTriggerPosition;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public String toString() {
        return "TriggerRegion{" +
                "rectangle=" + rectangle +
                ", triggerPosition=" + chunkTriggerPosition +
                '}';
    }
}
