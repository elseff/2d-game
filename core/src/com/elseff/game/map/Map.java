package com.elseff.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.map.chunk.TriggerPosition;
import com.elseff.game.model.GameObject;

import java.util.Optional;

public class Map {
    private final MyGdxGame game;
    private final Array<Chunk> chunks;
    private Chunk currentChunk;

    public Map(MyGdxGame game) {
        this.game = game;
        this.chunks = new Array<>();
        addChunk(0, 0, null);
        currentChunk = chunks.get(0);
    }

    public void addChunk(Vector2 position, TriggerPosition exclude) {
        addChunk(position.x, position.y, exclude);
    }

    public int addChunk(float xPos, float yPos, TriggerPosition exclude) {
        int id = chunks.size;
        Chunk chunk = new Chunk(id, game, xPos, yPos);
        chunk.initTriggers(exclude);
        chunks.add(chunk);
        return id;
    }

    public boolean isAreaClear(Rectangle rectangle) {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            for (int j = 0; j < chunk.getObjects().size; j++) {
                GameObject gameObject = chunk.getObjects().get(j);
                if (gameObject.getRectangle().overlaps(rectangle)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Chunk getChunkById(Integer id) {
        return chunks.get(id);
    }

    public Array<Chunk> getChunks() {
        return chunks;
    }

    public void setCurrentChunk(Chunk currentChunk) {
        this.currentChunk = currentChunk;
    }

    public Chunk getCurrentChunk() {
        return currentChunk;
    }

    public Optional<Chunk> getChunkByPosition(float x, float y) {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if (chunk.getPosition().x == x && chunk.getPosition().y == y) {
                return Optional.of(chunk);
            }
        }
        return Optional.empty();
    }

    public Optional<Chunk> getChunkByPosition(Vector2 position) {
        return getChunkByPosition(position.x, position.y);
    }
}
