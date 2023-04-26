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
import com.elseff.game.model.Enemy;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Player;
import com.elseff.game.model.Slime;
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
        this.chunks = new Array<>();
        addChunk(0, 0);
        this.currentChunk = chunks.get(0);
        this.player = gameScreen.getPlayer();
        this.batch = game.getBatch();
        this.shapeRenderer = game.getShapeRenderer();
        this.tmpVector = new Vector2();
        this.enemies = new Array<>();
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
            if (chunk.getPosition().x == x && chunk.getPosition().y == y) {
                return Optional.of(chunk);
            }
        }
        return Optional.empty();
    }

    public void update() {
        checkGenerate();
        updateCurrentChunk();
        checkDeleteTriggers();
        updateKillMonsters();
    }

    private void updateKillMonsters(){
        Array<GameObject> objects = currentChunk.getObjects();
        for (int i = 0; i < objects.size; i++) {
            GameObject gameObject = objects.get(i);
            if (gameObject.getClass().equals(Slime.class)){
                Slime slime = (Slime) gameObject;
                if (slime.getHp()<0){
                    currentChunk.getObjects().removeValue(slime, true);
                }
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
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if(chunk.getRectangle().overlaps(player.getChunkGeneratorRectangle())) {
                chunk.render();
            }
        }
        shapeRenderer.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setColor(ChunkTrigger.getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if(chunk.getRectangle().overlaps(player.getChunkGeneratorRectangle())) {
                chunk.renderTriggers();
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            if(chunk.getRectangle().overlaps(player.getChunkGeneratorRectangle())) {
                chunk.renderObjects(delta);
            }
        }
    }

    public boolean isAreaClear(Rectangle rectangle) {
        for (int i = 0; i < chunks.size; i++) {
            Chunk chunk = chunks.get(i);
            for (int j = 0; j < chunk.getObjects().size; j++) {
                GameObject gameObject = chunk.getObjects().get(j);
                if (gameObject.getRectangle().overlaps(rectangle) && !gameObject.getClass().equals(Slime.class)) {
                    return false;
                }
            }
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

    private void checkGenerate() {
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
