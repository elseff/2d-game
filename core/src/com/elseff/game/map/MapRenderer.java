package com.elseff.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.ChunkRenderer;
import com.elseff.game.map.chunk.trigger.ChunkTriggerRenderer;

public class MapRenderer {
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final Map map;
    private final ChunkTriggerRenderer chunkTriggerRenderer;
    private final ChunkRenderer chunkRenderer;

    public MapRenderer(MyGdxGame game) {
        this.map = new Map(game);
        this.shapeRenderer = game.getShapeRenderer();
        this.batch = game.getBatch();
        this.chunkTriggerRenderer = new ChunkTriggerRenderer(game, map);
        this.chunkRenderer = new ChunkRenderer(game, map);
    }

    public void render(float delta) {
        chunkTriggerRenderer.render();
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        chunkRenderer.render();
        shapeRenderer.end();

        batch.begin();
        chunkRenderer.renderObjects(delta);
    }

    public Map getMap() {
        return map;
    }
}
