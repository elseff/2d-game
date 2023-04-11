package com.elseff.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.TriggerRegionRenderer;

public class MapRenderer {
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final Map map;
    private final TriggerRegionRenderer renderer;
    public MapRenderer(MyGdxGame game) {
        this.map = new Map(game);
        this.shapeRenderer = game.getShapeRenderer();
        this.batch = game.getBatch();
        renderer = new TriggerRegionRenderer(game, map);
    }

    public void render(float delta) {
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < map.getChunks().size; i++)
            map.getChunks().get(i).render();
        shapeRenderer.end();

        batch.begin();
        for (int i = 0; i < map.getChunks().size; i++) {
            map.getChunks().get(i).renderObjects(delta);
        }

        renderer.render();
    }

    public Map getMap() {
        return map;
    }
}
