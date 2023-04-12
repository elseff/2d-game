package com.elseff.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.map.chunk.ChunkController;
import com.elseff.game.model.GameObject;

public class MapController {
    private final ChunkController chunkController;
    private final Map map;

    public MapController(MyGdxGame game, Map map) {
        this.chunkController = new ChunkController(game);
        this.map = map;
    }

    public void init() {
    }

    public void update() {
        chunkController.update();
        updateCurrentChunk();
    }

    private void updateCurrentChunk() {
        chunkController.updateCurrentChunk();
    }


    public boolean isAreaClear(Rectangle rectangle) {
        for (int i = 0; i < map.getChunks().size; i++) {
            Chunk chunk = map.getChunks().get(i);
            for (int j = 0; j < chunk.getObjects().size; j++) {
                GameObject gameObject = chunk.getObjects().get(j);
                if (gameObject.getRectangle().overlaps(rectangle)) {
                    return false;
                }
            }
        }
        return true;
    }
}
