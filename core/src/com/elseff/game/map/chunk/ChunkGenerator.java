package com.elseff.game.map.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.Map;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Player;
import com.elseff.game.model.box.BigWoodenBox;
import com.elseff.game.model.box.CardBox;
import com.elseff.game.model.box.SmallCardBox;
import com.elseff.game.model.box.WoodenBox;

public class ChunkGenerator {
    private final MyGdxGame game;
    private final Player player;
    private final Map map;
    private final int countRandomObjects;
    public ChunkGenerator(MyGdxGame game) {
        this.game = game;
        this.player = game.getScreen().getPlayer();
        this.map = game.getScreen().getMapRenderer().getMap();
        this.countRandomObjects = 1;
    }

    public void init() {
        Array<Chunk> chunks = map.getChunks();
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            fillRandomObjects(chunk.getId());
            chunk.initTriggers(null);
        }
    }


    public void update() {
        checkGenerate();
        checkDeleteTriggers();
    }

    private void checkDeleteTriggers() {
        Array<Chunk> chunks = map.getChunks();
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            Array<TriggerRegion> triggers = chunk.getTriggers();
            for (int j = 0; j < triggers.size; j++) {
                TriggerRegion trigger = triggers.get(j);
                switch (trigger.getTriggerPosition()) {
                    case BOTTOM -> {
                        if (map.getChunkByPosition(chunk.getPosition().x, chunk.getPosition().y - chunk.getRectangle().height).isPresent())
                            chunk.deleteTrigger(trigger.getTriggerPosition());
                    }
                    case TOP -> {
                        if (map.getChunkByPosition(chunk.getPosition().x, chunk.getPosition().y + chunk.getRectangle().height).isPresent())
                            chunk.deleteTrigger(trigger.getTriggerPosition());
                    }
                    case RIGHT -> {
                        if (map.getChunkByPosition(chunk.getPosition().x + chunk.getRectangle().width, chunk.getPosition().y).isPresent())
                            chunk.deleteTrigger(trigger.getTriggerPosition());
                    }
                    case LEFT -> {
                        if (map.getChunkByPosition(chunk.getPosition().x - chunk.getRectangle().width, chunk.getPosition().y).isPresent())
                            chunk.deleteTrigger(trigger.getTriggerPosition());
                    }
                }
            }
        }
    }

    private void checkGenerate() {
        Chunk currentChunk = map.getCurrentChunk();
        Array<TriggerRegion> triggers = currentChunk.getTriggers();
        for (int i = 0; i < triggers.size; i++) {
            TriggerRegion trigger = triggers.get(i);
            if (player.getRectangle().overlaps(trigger.getRectangle())) {
                deleteTrigger(currentChunk, trigger);
                switch (trigger.getTriggerPosition()) {
                    case TOP -> generateNewChunk(
                            currentChunk.getRectangle().x,
                            currentChunk.getPosition().y + currentChunk.getRectangle().height,
                            TriggerPosition.BOTTOM);
                    case LEFT -> generateNewChunk(
                            currentChunk.getPosition().x - currentChunk.getRectangle().width,
                            currentChunk.getRectangle().y,
                            TriggerPosition.RIGHT);
                    case RIGHT -> generateNewChunk(
                            currentChunk.getRectangle().x + currentChunk.getRectangle().width,
                            currentChunk.getRectangle().y,
                            TriggerPosition.LEFT);
                    case BOTTOM -> generateNewChunk(
                            currentChunk.getRectangle().x,
                            currentChunk.getRectangle().y - currentChunk.getRectangle().height,
                            TriggerPosition.TOP);
                }
            }
        }
    }

    private void generateNewChunk(float x, float y, TriggerPosition exclude) {
        int id = map.addChunk(x, y, exclude);
        fillRandomObjects(id);
    }

    private void deleteTrigger(Chunk chunk, TriggerRegion trigger) {
        chunk.deleteTrigger(trigger.getTriggerPosition());
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
            randomBox.getPosition().set(randomPosition(chunk, randomBox));
            chunk.addGameObject(randomBox);
        }
    }

    private Vector2 randomPosition(Chunk chunk, GameObject gameObject) {
        Vector2 result = new Vector2();
        result.x = ((int) (Math.random() * (chunk.getWidth())))
                * chunk.getCellSize()
                + gameObject.getRectangle().width / 2
                + chunk.getPosition().x;
        result.y = ((int) (Math.random() * (chunk.getHeight())))
                * chunk.getCellSize()
                + gameObject.getRectangle().height / 2
                + chunk.getPosition().y;
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

