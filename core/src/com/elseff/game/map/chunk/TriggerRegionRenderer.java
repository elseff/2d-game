package com.elseff.game.map.chunk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;

public class TriggerRegionRenderer {
    private final MyGdxGame game;
    private final ShapeRenderer shapeRenderer;
    private final Map map;

    public TriggerRegionRenderer(MyGdxGame game, Map map) {
        this.game = game;
        this.shapeRenderer = game.getShapeRenderer();
        this.map = map;
    }

    public void render() {
        if (game.isDebug()) {
            game.getBatch().end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setColor(TriggerRegion.getColor());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Array<Chunk> chunks = map.getChunks();
            for (int i = 0; i < chunks.size; i++) {
                Chunk chunk = chunks.get(i);
                Array<TriggerRegion> triggers = chunk.getTriggers();
                for (int j = 0; j < triggers.size; j++) {
                    TriggerRegion triggerRegion = triggers.get(j);
                    shapeRenderer.rect(
                            triggerRegion.getRectangle().x,
                            triggerRegion.getRectangle().y,
                            triggerRegion.getRectangle().width,
                            triggerRegion.getRectangle().height);
                }
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            game.getBatch().begin();
        }

    }
}
