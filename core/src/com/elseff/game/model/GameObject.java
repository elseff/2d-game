package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;

public abstract class GameObject {
    private final Vector2 position;
    private final MyGdxGame game;
    private final Rectangle rectangle;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Color rectColor;

    protected GameObject(MyGdxGame game, float x, float y) {
        this.game = game;
        this.position = new Vector2(x, y);
        this.shapeRenderer = game.getShapeRenderer();
        this.font = game.getFont();
        this.batch = game.getBatch();
        this.rectColor = new Color(0, 1, 0, 0.5f);
        this.shapeRenderer.setColor(rectColor);
        this.rectangle = new Rectangle(x, y, 0, 0);
    }

    protected GameObject(MyGdxGame game, Vector2 position) {
        this(game, position.x, position.y);
    }

    public void render(float delta) {
        update();
        if (getGame().isDebug()) {
            font.draw(batch,
                    String.format("(%.1f; %.1f)", getPosition().x, getPosition().y),
                    getPosition().x - getRectangle().width / 2f,
                    getPosition().y - getRectangle().height / 2f);
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            this.shapeRenderer.setColor(rectColor);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }
    }

    public void update() {
        this.rectangle.set(getRectangle());
    }

    public abstract Rectangle getRectangle();

    public Vector2 getPosition() {
        return position;
    }

    public MyGdxGame getGame() {
        return game;
    }
}
