package com.elseff.game.map.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.trigger.ChunkTrigger;
import com.elseff.game.map.chunk.trigger.ChunkTriggerPosition;
import com.elseff.game.model.FireBall;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Player;
import com.elseff.game.model.Slime;
import com.elseff.game.model.box.SmallCardBox;
import com.elseff.game.model.box.WoodenBox;
import com.elseff.game.screen.GameScreen;

import java.util.Objects;

public class Chunk {
    private final Integer id;
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final Vector2 position;
    private final Rectangle rectangle;
    private final Array<GameObject> objects;
    private final Array<ChunkTrigger> triggers;

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private final Player player;

    private final int cellSize = 32; // one cell size
    private final int width = 32; // count cells by horizontal
    private final int height = 32; // count cells by vertical

    private final int countRandomObjects;
    private final int countRandomMonsters;

    private boolean isCurrent = false;
    private boolean hasTriggers = true;
    private Color color;

    public Chunk(Integer id, MyGdxGame game, GameScreen gameScreen, float x, float y) {
        this.id = id;
        this.game = game;
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.objects = new Array<>();
        this.color = Color.WHITE;
        this.rectangle = new Rectangle(position.x, position.y, getWidthPixels(), getHeightPixels());
        this.triggers = new Array<>();
        this.batch = game.getBatch();
        this.shapeRenderer = game.getShapeRenderer();
        this.font = game.getFont();
        this.player = gameScreen.getPlayer();
        this.countRandomObjects = 10;
        this.countRandomMonsters = 3;
        initTriggers();
    }

    public Chunk(Integer id, MyGdxGame game, GameScreen gameScreen, Vector2 position) {
        this(id, game, gameScreen, position.x, position.y);
    }

    public void initTriggers() {
        ChunkTrigger bottomChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.BOTTOM, this);
        ChunkTrigger rightChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.RIGHT, this);
        ChunkTrigger topChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.TOP, this);
        ChunkTrigger leftChunkTrigger = new ChunkTrigger(ChunkTriggerPosition.LEFT, this);
        addTrigger(rightChunkTrigger);
        addTrigger(topChunkTrigger);
        addTrigger(leftChunkTrigger);
        addTrigger(bottomChunkTrigger);
    }

    public void render() {
        if (game.isDebug()) {
            shapeRenderer.setColor(color);
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    shapeRenderer.rect(
                            j * cellSize + position.x,
                            k * cellSize + position.y,
                            cellSize,
                            cellSize);
                }
            }

            shapeRenderer.end();

            batch.begin();
            font.draw(game.getBatch(),
                    String.format("%s (%.1f; %.1f)", id, position.x, position.y),
                    position.x,
                    position.y);
            batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        }
    }

    public void update() {
        updateColor();
    }

    private void updateColor() {
        if (isCurrent)
            color = Color.RED;
        else
            color = Color.WHITE;
    }

    public void renderObjects(float delta) {
        for (int j = 0; j < objects.size; j++) {
            GameObject object = objects.get(j);
            if (object.getRectangle().overlaps(player.getChunkGeneratorRectangle())
                    || player.getChunkGeneratorRectangle().contains(object.getRectangle())) {
                object.render(delta);
            }
        }
    }

    public void renderTriggers() {
        if (hasTriggers) {
            if (game.isDebug()) {
                for (int j = 0; j < triggers.size; j++) {
                    ChunkTrigger chunkTrigger = triggers.get(j);
                    shapeRenderer.rect(
                            chunkTrigger.getRectangle().x,
                            chunkTrigger.getRectangle().y,
                            chunkTrigger.getRectangle().width,
                            chunkTrigger.getRectangle().height);
                }
            }
        }
    }

    public void fillRandomObjects() {
        FireBall fireBall = new FireBall(game, gameScreen, 0, 0, 2.0f, true, false);

        for (int j = 0; j < countRandomObjects; j++) {
            int random = (int) (Math.random() * 3 + 1);
            GameObject randomBox;
            if (random == 1) {
                randomBox = new SmallCardBox(game, gameScreen, 0, 0);
            } else {
                randomBox = new WoodenBox(game, gameScreen, 0, 0);
            }
            do {
                randomBox.getPosition().set(gameScreen.getMap().randomPosition(this, randomBox));
                fireBall.getPosition().set(gameScreen.getMap().randomPosition(this, fireBall));
            } while (randomBox.getRectangle().overlaps(player.getRectangle()));
            addGameObject(randomBox);
        }
        addGameObject(fireBall);
    }

    public void fillRandomMonsters() {
        for (int i = 0; i < countRandomMonsters; i++) {
            Slime slime = new Slime(game, gameScreen, 0, 0);
            FireBall fireBall = new FireBall(game, gameScreen, 0, 0, 2.0f, true, false);
            for (int j = 0; j < getObjects().size; j++) {
                GameObject gameObject = getObjects().get(i);
                do {
                    slime.getPosition().set(gameScreen.getMap().randomPosition(this, slime));
                } while (slime.getRectangle().overlaps(gameObject.getRectangle()));
            }
            objects.add(slime);
            gameScreen.getMap().getEnemies().add(slime);
        }
    }

    public void addTrigger(ChunkTrigger trigger) {
        this.triggers.add(trigger);
    }

    public void deleteTriggerByTriggerPosition(ChunkTriggerPosition chunkTriggerPosition) {
        for (int i = 0; i < triggers.size; i++) {
            ChunkTrigger chunkTrigger = triggers.get(i);

            if (chunkTrigger.getTriggerPosition().name().equals(chunkTriggerPosition.name()))
                triggers.removeValue(chunkTrigger, true);
        }
        if (triggers.size == 0) {
            hasTriggers = false;
        }
    }

    public void deleteTrigger(ChunkTrigger trigger) {
        triggers.removeValue(trigger, true);
    }

    public void addGameObject(GameObject gameObject) {
        objects.add(gameObject);
    }

    public void deleteGameObject(GameObject gameObject) {
        objects.removeValue(gameObject, true);
    }

    public Array<ChunkTrigger> getTriggers() {
        return triggers;
    }

    public Integer getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Array<GameObject> getObjects() {
        return objects;
    }

    public Color getColor() {
        return color;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthPixels() {
        return width * cellSize;
    }

    public int getHeightPixels() {
        return height * cellSize;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return id.equals(chunk.id) && position.equals(chunk.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "id=" + id +
                ", color=" + color +
                ", position=" + position +
                ", triggers=" + triggers +
                '}';
    }

    public boolean isHasTriggers() {
        return hasTriggers;
    }
}
