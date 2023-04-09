package com.elseff.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.model.GameObject;

import java.util.Objects;

public class Chunk {
    private MyGdxGame game;
    private SpriteBatch batch;
    private final Integer id;
    private final ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Color color;
    private final Vector2 position;
    private final int cellSize = 32; // one cell size
    private final int width = 16;
    private final int height = 16;
    private Rectangle rectangle;
    private boolean isCurrent = false;

    private final Array<GameObject> objects;

    public Chunk(Integer id, MyGdxGame game, float x, float y) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.objects = new Array<>();
        this.shapeRenderer = game.getShapeRenderer();
        this.color = Color.GREEN;
        this.rectangle = new Rectangle(position.x, position.y, getWidthPixels(), getHeightPixels());
        this.font = game.getFont();
        this.game = game;
        this.batch = game.getBatch();
    }

    public Chunk(Integer id, MyGdxGame game, Vector2 position) {
        this(id, game, position.x, position.y);
        this.rectangle = new Rectangle(position.x, position.y, getWidthPixels(), getHeightPixels());
        this.font = game.getFont();
        this.game = game;
        this.batch = game.getBatch();
    }

    public void render() {
        if (game.isDebug()) {
            Color oldColor = shapeRenderer.getColor();
            shapeRenderer.setColor(color);
            for (int j = 0; j < getWidth(); j++) {
                for (int k = 0; k < getHeight(); k++) {
                    shapeRenderer.rect(
                            j * getCellSize() + getPosition().x,
                            k * getCellSize() + getPosition().y,
                            getCellSize(),
                            getCellSize());
                }
            }
            shapeRenderer.end();
            batch.begin();
            font.draw(game.getBatch(),
                    String.format("%s (%.1f; %.1f)", getId(), getPosition().x, getPosition().y),
                    getPosition().x,
                    getPosition().y);
            batch.end();
            shapeRenderer.setColor(oldColor);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        }
    }

    public void renderObjects(float delta) {
        for (int j = 0; j < getObjects().size; j++) {
            GameObject object = getObjects().get(j);
            object.render(delta);
        }
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

    public void addGameObject(GameObject gameObject) {
        objects.add(gameObject);
    }

    public int getHeightPixels() {
        return height * cellSize;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Array<GameObject> getObjects() {
        return objects;
    }

    public Integer getId() {
        return id;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
