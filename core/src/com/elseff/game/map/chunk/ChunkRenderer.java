package com.elseff.game.map.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.model.GameObject;

public class ChunkRenderer {
    private final MyGdxGame game;
    private final Map map;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    public ChunkRenderer(MyGdxGame game, Map map) {
        this.game = game;
        this.map = map;
        this.font = game.getFont();
        this.shapeRenderer = game.getShapeRenderer();
        this.batch = game.getBatch();
    }

    public void render() {
        Array<Chunk> chunks = map.getChunks();
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if (game.isDebug()) {
                Color oldColor = shapeRenderer.getColor();
                shapeRenderer.setColor(chunk.getColor());
                for (int j = 0; j < chunk.getWidth(); j++) {
                    for (int k = 0; k < chunk.getHeight(); k++) {
                        shapeRenderer.rect(
                                j * chunk.getCellSize() + chunk.getPosition().x,
                                k * chunk.getCellSize() + chunk.getPosition().y,
                                chunk.getCellSize(),
                                chunk.getCellSize());
                    }
                }
                shapeRenderer.end();
                batch.begin();
                font.draw(game.getBatch(),
                        String.format("%s (%.1f; %.1f)", chunk.getId(), chunk.getPosition().x, chunk.getPosition().y),
                        chunk.getPosition().x,
                        chunk.getPosition().y);
                batch.end();
                shapeRenderer.setColor(oldColor);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            }
        }
    }

    public void renderObjects(float delta) {
        Array<Chunk> chunks = map.getChunks();
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            for (int j = 0; j < chunk.getObjects().size; j++) {
                GameObject object = chunk.getObjects().get(j);
                object.render(delta);
            }
        }
    }
}
