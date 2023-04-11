package com.elseff.game.map;

import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.ChunkGenerator;

public class MapController {
    private final ChunkGenerator chunkGenerator;

    public MapController(MyGdxGame game) {
        this.chunkGenerator = new ChunkGenerator(game);
    }

    public void init() {
        chunkGenerator.init();
    }

    public void update() {
        chunkGenerator.update();
        updateCurrentChunk();
    }

    private void updateCurrentChunk() {
        chunkGenerator.updateCurrentChunk();
    }
}
