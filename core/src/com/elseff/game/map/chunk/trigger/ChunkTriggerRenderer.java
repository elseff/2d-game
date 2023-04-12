package com.elseff.game.map.chunk.trigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.map.chunk.Chunk;

public class ChunkTriggerRenderer {
    private final MyGdxGame game;
    private final ShapeRenderer shapeRenderer;
    private final Map map;

    public ChunkTriggerRenderer(MyGdxGame game, Map map) {
        this.game = game;
        this.shapeRenderer = game.getShapeRenderer();
        this.map = map;
    }

    public void render() {
        if (game.isDebug()) {
            game.getBatch().end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setColor(ChunkTrigger.getColor());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Array<Chunk> chunks = map.getChunks();
            for (int i = 0; i < chunks.size; i++) {
                Chunk chunk = chunks.get(i);
                Array<ChunkTrigger> triggers = chunk.getTriggers();
                for (int j = 0; j < triggers.size; j++) {
                    ChunkTrigger chunkTrigger = triggers.get(j);
                    shapeRenderer.rect(
                            chunkTrigger.getRectangle().x,
                            chunkTrigger.getRectangle().y,
                            chunkTrigger.getRectangle().width,
                            chunkTrigger.getRectangle().height);
                }
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            game.getBatch().begin();
        }

    }
}
