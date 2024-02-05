package com.elseff.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.map.chunk.trigger.ChunkTrigger;
import com.elseff.game.model.enemy.Enemy;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Player;
import com.elseff.game.model.enemy.Slime;
import com.elseff.game.screen.GameScreen;

import java.util.Optional;

public class Map {
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final Array<Chunk> chunks;
    private final Array<Enemy> enemies;
    private Chunk currentChunk;

    private final Player player;

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    private final Vector2 tmpVector;

    public Map(MyGdxGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        chunks = new Array<>();
        enemies = new Array<>();

        addChunk(0, 0);
        currentChunk = chunks.get(0);

        player = gameScreen.getPlayer();
        tmpVector = new Vector2();
    }

    public void addChunk(Vector2 position) {
        addChunk(position.x, position.y);
    }

    public Chunk addChunk(float xPos, float yPos) {
        int id = chunks.size;
        Chunk chunk = new Chunk(id, game, gameScreen, xPos, yPos);
        chunks.add(chunk);
        return chunk;
    }

    public Optional<Chunk> getChunkByPosition(float x, float y) {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if (chunk.getPosition().x == x && chunk.getPosition().y == y)
                return Optional.of(chunk);
        }
        return Optional.empty();
    }

    public void update() {
        checkGenerateChunk();
        updateCurrentChunk();
        checkDeleteTriggers();
        updateKillMonsters();
    }

    private void updateKillMonsters(){
        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getClass().equals(Slime.class)){
                Slime slime = (Slime) enemy;
                if (slime.getHp()<0)
                    enemies.removeValue(slime, true);
            }
        }
    }

    private void updateCurrentChunk() {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if (chunk.getRectangle().overlaps(player.getRectangle())) {
                chunk.setCurrent(true);
                chunk.update();
                setCurrentChunk(chunk);
            } else {
                chunk.setCurrent(false);
                chunk.update();
            }
        }
    }

    public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if(player.getChunkGeneratorRectangle().overlaps(chunk.getRectangle()))
                chunk.render(delta);
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(ChunkTrigger.getColor());
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if(chunk.getRectangle().overlaps(player.getChunkGeneratorRectangle()))
                chunk.renderTriggers();
        }
        shapeRenderer.end();

        renderEnemies(delta);
    }

    private void renderEnemies(float delta){
        game.getBatch().begin();
        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if(player.getChunkGeneratorRectangle().overlaps(enemy.getRectangle()))
                enemy.render(delta);
        }
        game.getBatch().end();
    }

    public boolean isAreaClear(Rectangle rectangle) {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            for (int j = 0; j < chunk.getObjects().size; j++) {
                GameObject gameObject = chunk.getObjects().get(j);
                if (gameObject.getRectangle().overlaps(rectangle))
                    return false;
            }
        }
//        for (int i = 0; i < enemies.size; i++) {
//            Enemy enemy = enemies.get(i);
//            if (enemy.getRectangle().overlaps(rectangle))
//                return false;
//        }
        return true;
    }

    public boolean isAreaClearByPoint(Vector2 point){
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            for (int j = 0; j < chunk.getObjects().size; j++) {
                GameObject gameObject = chunk.getObjects().get(j);
                if (gameObject.getRectangle().contains(point))
                    return false;
            }
        }
        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getRectangle().contains(point))
                return false;
        }
        return true;
    }

    public Vector2 randomPosition(Chunk chunk, GameObject gameObject) {
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

    private void checkGenerateChunk() {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if (chunk.isHasTriggers()) {
                if (chunk.getRectangle().overlaps(player.getChunkGeneratorRectangle())) {
                    Array<ChunkTrigger> triggers = chunk.getTriggers();
                    for (int j = 0; j < triggers.size; j++) {
                        ChunkTrigger trigger = triggers.get(j);
                        if (player.getChunkGeneratorRectangle().overlaps(trigger.getRectangle())) {
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                            switch (trigger.getTriggerPosition()) {
                                case TOP -> {
                                    tmpVector.set(chunk.getRectangle().x,
                                            chunk.getPosition().y + chunk.getRectangle().height);
                                    if (getChunkByPosition(tmpVector).isEmpty())
                                        generateNewChunk(tmpVector.x, tmpVector.y);
                                }
                                case LEFT -> {
                                    tmpVector.set(chunk.getPosition().x - chunk.getRectangle().width,
                                            chunk.getRectangle().y);
                                    if (getChunkByPosition(tmpVector).isEmpty())
                                        generateNewChunk(tmpVector.x, tmpVector.y);
                                }
                                case RIGHT -> {
                                    tmpVector.set(chunk.getRectangle().x + chunk.getRectangle().width,
                                            chunk.getRectangle().y);
                                    if (getChunkByPosition(tmpVector).isEmpty())
                                        generateNewChunk(tmpVector.x, tmpVector.y);
                                }
                                case BOTTOM -> {
                                    tmpVector.set(chunk.getRectangle().x,
                                            chunk.getRectangle().y - chunk.getRectangle().height);
                                    if (getChunkByPosition(tmpVector).isEmpty())
                                        generateNewChunk(tmpVector.x, tmpVector.y);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateNewChunk(float x, float y) {
        Chunk chunk = addChunk(x, y);
        checkDeleteTriggers();
        chunk.fillRandomObjects();
        chunk.fillRandomMonsters();
        chunk.fillRandomFood();
    }

    private void checkDeleteTriggers() {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            Array<ChunkTrigger> triggers = chunk.getTriggers();
            for (int j = 0; j < triggers.size; j++) {
                ChunkTrigger trigger = triggers.get(j);
                switch (trigger.getTriggerPosition()) {
                    case BOTTOM -> {
                        if (getChunkByPosition(chunk.getPosition().x, chunk.getPosition().y - chunk.getRectangle().height).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                    case TOP -> {
                        if (getChunkByPosition(chunk.getPosition().x, chunk.getPosition().y + chunk.getRectangle().height).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                    case RIGHT -> {
                        if (getChunkByPosition(chunk.getPosition().x + chunk.getRectangle().width, chunk.getPosition().y).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                    case LEFT -> {
                        if (getChunkByPosition(chunk.getPosition().x - chunk.getRectangle().width, chunk.getPosition().y).isPresent())
                            chunk.deleteTriggerByTriggerPosition(trigger.getTriggerPosition());
                    }
                }
            }
        }
    }

    public Optional<Chunk> getChunkByPosition(Vector2 position) {
        return getChunkByPosition(position.x, position.y);
    }

    public Chunk getChunkById(Integer id) {
        return chunks.get(id);
    }

    public Array<Chunk> getChunks() {
        return chunks;
    }

    public void setCurrentChunk(Chunk currentChunk) {
        this.currentChunk = currentChunk;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public Chunk getCurrentChunk() {
        return currentChunk;
    }

}
