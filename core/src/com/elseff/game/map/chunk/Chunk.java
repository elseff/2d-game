package com.elseff.game.map.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.map.chunk.trigger.ChunkTrigger;
import com.elseff.game.map.chunk.trigger.ChunkTriggerPosition;
import com.elseff.game.model.GameObject;

import java.util.Objects;

public class Chunk {
    private final Integer id;
    private final Vector2 position;
    private final Rectangle rectangle;
    private final Array<GameObject> objects;
    private final Array<ChunkTrigger> triggers;

    private final int cellSize = 32; // one cell size
    private final int width = 16; // count cells by horizontal
    private final int height = 16; // count cells by vertical

    private boolean isCurrent = false;
    private Color color;

    public Chunk(Integer id, float x, float y) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.objects = new Array<>();
        this.color = Color.GREEN;
        this.rectangle = new Rectangle(position.x, position.y, getWidthPixels(), getHeightPixels());
        this.triggers = new Array<>();
    }

    public Chunk(Integer id, Vector2 position) {
        this(id, position.x, position.y);
    }

    public void initTriggers() {
        ChunkTrigger bottomChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.BOTTOM, this);
        ChunkTrigger rightChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.RIGHT, this);
        ChunkTrigger topChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.TOP, this);
        ChunkTrigger leftChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.LEFT, this);
        addTrigger(rightChunkTrigger);
        addTrigger(topChunkTrigger);
        addTrigger(leftChunkTrigger);
        addTrigger(bottomChunkTrigger);
    }

    public void addTrigger(ChunkTrigger trigger) {
        this.triggers.add(trigger);
    }

    public void deleteTriggerByTriggerPosition(ChunkTriggerPosition chunkTriggerPosition) {
        for (int i = 0; i < triggers.size; i++) {
            ChunkTrigger chunkTrigger = triggers.get(i);
            if (chunkTrigger.getTriggerPosition().name().equals(chunkTriggerPosition.name())) {
                triggers.removeValue(chunkTrigger, true);
            }
        }
    }

    public void deleteTrigger(ChunkTrigger trigger){
        triggers.removeValue(trigger, true);
    }

    public void addGameObject(GameObject gameObject) {
        objects.add(gameObject);
    }

    public void deleteGameObject(GameObject gameObject) {
        objects.removeValue(gameObject, true);
    }

    public Array<ChunkTrigger> getTriggers() {
        return triggers;
    }

    public Integer getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Array<GameObject> getObjects() {
        return objects;
    }

    public Color getColor() {
        return color;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthPixels() {
        return width * cellSize;
    }

    public int getHeightPixels() {
        return height * cellSize;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return id.equals(chunk.id) && position.equals(chunk.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "id=" + id +
                ", color=" + color +
                ", position=" + position +
                ", triggers=" + triggers +
                '}';
    }
}
