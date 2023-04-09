package com.elseff.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MapRenderer {
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final Map map;

    public MapRenderer(MyGdxGame game) {
        this.map = new Map(game);
        this.shapeRenderer = game.getShapeRenderer();
        this.batch = game.getBatch();
    }

    public void render(float delta) {
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < map.getChunks().size(); i++)
            map.getChunks().get(i).render();
        shapeRenderer.end();

        batch.begin();
        for (int i = 0; i < map.getChunks().size(); i++) {
            map.getChunks().get(i).renderObjects(delta);
        }
    }

    public Map getMap() {
        return map;
    }
}
