package com.elseff.game;

import com.badlogic.gdx.math.Vector2;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.box.CardBox;
import com.elseff.game.model.box.WoodenBox;

import java.util.HashMap;

public class Map {
    private final MyGdxGame game;
    private final java.util.Map<Integer, Chunk> chunks;
    private Chunk currentChunk;

    public Map(MyGdxGame game) {
        this.game = game;
        this.chunks = new HashMap<>();
        addChunk(0, 0);
        addChunk(512, 0);
        addChunk(0, 512);
        addChunk(512, 512);
        for (int i = 0; i < chunks.size(); i++) {
            CardBox cardBox = new CardBox(game, 0, 0);
            WoodenBox woodenBox = new WoodenBox(game, 0, 0);
            Chunk chunk = chunks.get(i);
            cardBox.getPosition().set(randomPosition(chunk, cardBox));
            woodenBox.getPosition().set(randomPosition(chunk, woodenBox));
            chunk.addGameObject(cardBox);
            chunk.addGameObject(woodenBox);
        }
        this.currentChunk = getChunkById(0);
    }

    private Vector2 randomPosition(Chunk chunk, GameObject gameObject) {
        Vector2 result = new Vector2();
        result.x = ((int) (Math.random() * (chunk.getWidth())))
                * chunk.getCellSize()
                + gameObject.getRectangle().width / 2
                + chunk.getPosition().x;
        result.y = ((int) (Math.random() * (chunk.getHeight())))
                * chunk.getCellSize()
                + gameObject.getRectangle().height / 2
                + chunk.getPosition().y;
        return result;
    }

    public void addChunk(Vector2 position) {
        int id = chunks.size();
        Chunk chunk = new Chunk(id, game, position);
        chunks.put(chunk.getId(), chunk);
    }

    public void addChunk(float xPos, float yPos) {
        int id = chunks.size();
        Chunk chunk = new Chunk(id, game, xPos, yPos);
        chunks.put(chunk.getId(), chunk);
    }

    public Chunk getChunkById(Integer id){
        return chunks.get(id);
    }

    public java.util.Map<Integer, Chunk> getChunks() {
        return chunks;
    }

    public Chunk getCurrentChunk() {
        return currentChunk;
    }

    public void setCurrentChunk(Chunk currentChunk) {
        this.currentChunk = currentChunk;
    }
}
