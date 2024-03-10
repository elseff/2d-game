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
import com.elseff.game.model.GameObject;
import com.elseff.game.model.box.Box;
import com.elseff.game.model.box.RandomTextureBox;
import com.elseff.game.model.box.SmallCardBox;
import com.elseff.game.model.enemy.Slime;
import com.elseff.game.model.food.Food;
import com.elseff.game.model.food.FriedChicken;
import com.elseff.game.model.player.Player;
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
    private final Array<Food> foodArray;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private final Player player;

    private final int cellSize = 32; // one cell size
    private final int width = 32; // count cells by horizontal
    private final int height = 32; // count cells by vertical

    private final int countRandomObjects;
    private final int countRandomMonsters;
    private final int countRandomFood;

    private boolean isCurrent = false;
    private boolean hasTriggers = true;

    private final Color isCurrentColor;
    private final Color isNotCurrentColor;
    private Color currentColor;

    public Chunk(Integer id, MyGdxGame game, GameScreen gameScreen, float x, float y) {
        this.id = id;
        this.game = game;
        this.gameScreen = gameScreen;
        position = new Vector2(x, y);
        objects = new Array<>();
        triggers = new Array<>();
        foodArray = new Array<>();
        isNotCurrentColor = new Color(1, 1, 1, 0.3f);
        isCurrentColor = new Color(0.3f, 1, 0.3f, 0.1f);
        currentColor = isNotCurrentColor;
        rectangle = new Rectangle(position.x, position.y, getWidthPixels(), getHeightPixels());
        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();
        font = game.getFont();
        player = gameScreen.getPlayer();
        countRandomObjects = 10;
        countRandomMonsters = 3;
        countRandomFood = 1;
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

    public void render(float delta) {
        renderCells();
        renderDebug();
        renderObjects(delta);
        renderFood(delta);
    }

    private void renderCells() {
        game.GRACEFUL_SHAPE_RENDERER_BEGIN(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                shapeRenderer.rect(
                        j * cellSize + position.x,
                        k * cellSize + position.y,
                        cellSize,
                        cellSize);
            }
        }
        game.GRACEFUL_SHAPE_RENDERER_END();
    }

    private void renderDebug() {
        if (!game.isDebug()) return;

        game.GRACEFUL_SHAPE_RENDERER_BEGIN();
        if (isCurrent()) {
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(position.x, position.y, getWidthPixels(), getHeightPixels());
        }

        game.GRACEFUL_SHAPE_RENDERER_END();
        font.draw(game.getBatch(),
                String.format("chunk:%s (%.1f; %.1f) - (%s)", id, position.x, position.y, getObjects().size),
                position.x + 2,
                position.y + 10);
        game.GRACEFUL_SHAPE_RENDERER_BEGIN();
    }

    private void renderObjects(float delta) {
        game.GRACEFUL_SHAPE_RENDERER_END();
        for (int j = 0; j < objects.size; j++) {
            GameObject object = objects.get(j);
            if (player.getChunkGeneratorRectangle().overlaps(object.getRectangle()))
                object.render(delta);
        }
        game.GRACEFUL_SHAPE_RENDERER_BEGIN();
    }

    public void renderTriggers() {
        if (!game.isDebug())
            return;
        if (!hasTriggers)
            return;

        game.GRACEFUL_SHAPE_RENDERER_BEGIN(ShapeRenderer.ShapeType.Filled);
        for (int j = 0; j < triggers.size; j++) {
            ChunkTrigger chunkTrigger = triggers.get(j);
            shapeRenderer.rect(
                    chunkTrigger.getRectangle().x,
                    chunkTrigger.getRectangle().y,
                    chunkTrigger.getRectangle().width,
                    chunkTrigger.getRectangle().height);
        }
        game.GRACEFUL_SHAPE_RENDERER_END();
    }

    public void renderFood(float delta) {
        game.GRACEFUL_SHAPE_RENDERER_END();
        for (int i = 0; i < foodArray.size; i++) {
            Food food = foodArray.get(i);
            if (player.getChunkGeneratorRectangle().overlaps(food.getRectangle())) {
                food.render(delta);
            }
        }
        game.GRACEFUL_SHAPE_RENDERER_BEGIN();
    }

    public void update() {
        updateColor();
    }

    private void updateColor() {
        if (isCurrent)
            currentColor = isCurrentColor;
        else
            currentColor = isNotCurrentColor;
    }

    public void fillRandomObjects() {
        for (int j = 0; j < countRandomObjects; j++) {
            int random = (int) (Math.random() * 3 + 1);
            Box randomBox;
            if (random == 1) {
                randomBox = new SmallCardBox(game, gameScreen, 0, 0);
            } else {
                randomBox = new RandomTextureBox(game, gameScreen, 0, 0);
            }
            setRandomPositionIncludingGameObjects(randomBox);
            addGameObject(randomBox);
        }
    }

    public void fillRandomFood() {
        for (int i = 0; i < countRandomFood; i++) {
            Food randomFood = new FriedChicken(game, 0, 0, gameScreen);
            setRandomPositionIncludingGameObjects(randomFood);
            foodArray.add(randomFood);
        }
    }

    public void fillRandomMonsters() {
        for (int i = 0; i < countRandomMonsters; i++) {
            Slime slime = new Slime(game, gameScreen, 0, 0);
            setRandomPositionIncludingGameObjects(slime);
            gameScreen.getMap().getEnemies().add(slime);
        }
    }

    private void setRandomPositionIncludingGameObjects(GameObject gameObject) {
        boolean findCollision;
        do {
            findCollision = false;
            gameObject.getPosition().set(gameScreen.getMap().randomPosition(this, gameObject));
            for (GameObject object : objects) {
                if (object.getRectangle().overlaps(gameObject.getRectangle())) {
                    findCollision = true;
                    break;
                }
            }
        } while (gameObject.getRectangle().overlaps(player.getRectangle()) || findCollision);
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

    public Color getCurrentColor() {
        return currentColor;
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

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
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
                ", color=" + currentColor +
                ", position=" + position +
                ", triggers=" + triggers +
                '}';
    }

    public boolean isHasTriggers() {
        return hasTriggers;
    }

    public Array<Food> getFoodArray() {
        return foodArray;
    }
}
