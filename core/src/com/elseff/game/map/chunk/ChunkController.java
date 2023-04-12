package com.elseff.game.map.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.map.chunk.trigger.ChunkTrigger;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Player;
import com.elseff.game.model.box.BigWoodenBox;
import com.elseff.game.model.box.CardBox;
import com.elseff.game.model.box.SmallCardBox;
import com.elseff.game.model.box.WoodenBox;

public class ChunkController {
    private final MyGdxGame game;
    private final Player player;
    private final Map map;
    private final int countRandomObjects;
    private final Vector2 tmpVector;

    public ChunkController(MyGdxGame game) {
        this.game = game;
        this.player = game.getScreen().getPlayer();
        this.map = game.getScreen().getMapRenderer().getMap();
        this.countRandomObjects = 5;
        tmpVector = new Vector2();
    }

    public void update() {
        checkGenerate();
        checkDeleteTriggers();
    }

    private void checkDeleteTriggers() {
        Array<Chunk> chunks = map.getChunks();
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            Array<ChunkTrigger> triggers = chunk.getTriggers();
            for (int j = 0; j < triggers.size; j++) {
                ChunkTrigger trigger = triggers.get(j);
                switch (trigger.getTriggerPosition()) {
                    case BOTTOM -> {
                        if (map.getChunkByPosition(chunk.getPosition().x, chunk.getPosition().y - chunk.getRectangle().height).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                    case TOP -> {
                        if (map.getChunkByPosition(chunk.getPosition().x, chunk.getPosition().y + chunk.getRectangle().height).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                    case RIGHT -> {
                        if (map.getChunkByPosition(chunk.getPosition().x + chunk.getRectangle().width, chunk.getPosition().y).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                    case LEFT -> {
                        if (map.getChunkByPosition(chunk.getPosition().x - chunk.getRectangle().width, chunk.getPosition().y).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                }
            }
        }
    }

    private void checkGenerate() {
        for (int i = 0; i < map.getChunks().size; i++) {
            Chunk chunk = map.getChunks().get(i);
            Array<ChunkTrigger> triggers = chunk.getTriggers();
            for (int j = 0; j < triggers.size; j++) {
                ChunkTrigger trigger = triggers.get(j);
                if (player.getChunkGeneratorRectangle().overlaps(trigger.getRectangle())) {
                    deleteTrigger(chunk, trigger);
                    switch (trigger.getTriggerPosition()) {
                        case TOP -> {
                            tmpVector.set(chunk.getRectangle().x,
                                    chunk.getPosition().y + chunk.getRectangle().height);
                            if (map.getChunkByPosition(tmpVector).isEmpty())
                                generateNewChunk(tmpVector.x, tmpVector.y);
                        }
                        case LEFT -> {
                            tmpVector.set(chunk.getPosition().x - chunk.getRectangle().width,
                                    chunk.getRectangle().y);
                            if (map.getChunkByPosition(tmpVector).isEmpty())
                                generateNewChunk(tmpVector.x, tmpVector.y);
                        }
                        case RIGHT -> {
                            tmpVector.set(chunk.getRectangle().x + chunk.getRectangle().width,
                                    chunk.getRectangle().y);
                            if (map.getChunkByPosition(tmpVector).isEmpty())
                                generateNewChunk(tmpVector.x, tmpVector.y);
                        }
                        case BOTTOM -> {
                            tmpVector.set(chunk.getRectangle().x,
                                    chunk.getRectangle().y - chunk.getRectangle().height);
                            if (map.getChunkByPosition(tmpVector).isEmpty())
                                generateNewChunk(tmpVector.x, tmpVector.y);
                        }
                    }
                }
            }
        }
    }

    private void generateNewChunk(float x, float y) {
        Chunk chunk = map.addChunk(x, y);
        checkDeleteTriggers();
        fillRandomObjects(chunk.getId());
    }

    private void deleteTrigger(Chunk chunk, ChunkTrigger trigger) {
        chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
    }

    private void fillRandomObjects(int chunkId) {
        Chunk chunk = map.getChunkById(chunkId);
        for (int j = 0; j < countRandomObjects; j++) {
            int random = (int) (Math.random() * 4);
            GameObject randomBox;
            switch (random) {
                case 1 -> randomBox = new WoodenBox(game, 0, 0);
                case 2 -> randomBox = new SmallCardBox(game, 0, 0);
                case 3 -> randomBox = new BigWoodenBox(game, 0, 0);
                default -> randomBox = new CardBox(game, 0, 0);
            }
            do {
                randomBox.getPosition().set(randomPosition(chunk, randomBox));
            } while (randomBox.getRectangle().overlaps(player.getRectangle()));
            chunk.addGameObject(randomBox);
        }
    }

    private Vector2 randomPosition(Chunk chunk, GameObject gameObject) {
        Vector2 result = new Vector2();
        result.x = ((int) (Math.random() * (chunk.getWidth() - 4))) // skip last 2 cells
                * chunk.getCellSize()
                + chunk.getPosition().x + chunk.getCellSize() * 2 // skip first 2 cells
                + gameObject.getRectangle().width / 2;
        result.y = ((int) (Math.random() * (chunk.getHeight() - 4))) // skip last 2 cells
                * chunk.getCellSize()
                + chunk.getPosition().y + chunk.getCellSize() * 2 // skip first 2 cells
                + gameObject.getRectangle().height / 2;
        return result;
    }

    public void updateCurrentChunk() {
        Array<Chunk> chunks = game.getScreen().getMapRenderer().getMap().getChunks();
        for (int i = 0; i < chunks.size; i++) {
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

