package com.elseff.game;

import com.badlogic.gdx.graphics.Color;
import com.elseff.game.model.Player;

public class MapController {
    private final MyGdxGame game;
    private final Map map;
    private final Player player;

    public MapController(MyGdxGame game) {
        this.game = game;
        this.map = game.getScreen().getMapRenderer().getMap();
        this.player = game.getScreen().getPlayer();
    }


    public void update() {
        currentChunk();
        checkGenerateNewChunk();
    }

    private void checkGenerateNewChunk() {

    }

    private void currentChunk() {
        java.util.Map<Integer, Chunk> chunks = game.getScreen().getMapRenderer().getMap().getChunks();
        for (int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.get(i);
            if (chunk.getRectangle().overlaps(player.getRectangle())) {
                chunk.setColor(Color.RED);
                chunk.setCurrent(true);
                map.setCurrentChunk(chunk);
            } else {
                chunk.setColor(Color.GREEN);
                chunk.setCurrent(false);
            }
        }
    }
}
