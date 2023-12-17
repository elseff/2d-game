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
import com.elseff.game.screen.GameScreen;

public abstract class GameObject {
    private final Vector2 position;
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Color rectColor;

    protected GameObject(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.shapeRenderer = game.getShapeRenderer();
        this.font = game.getFont();
        this.batch = game.getBatch();
        this.rectColor = new Color(0, 1, 0, 0.5f);
    }

    protected GameObject(MyGdxGame game, GameScreen gameScreen, Vector2 position) {
        this(game, position.x, position.y, gameScreen);
    }

    public void render(float delta) {
        if (getGame().isDebug()) {
            Color oldColor = font.getColor();
            font.setColor(Color.WHITE);
            font.draw(batch,
                    String.format("(%.1f; %.1f)", getPosition().x, getPosition().y),
                    getPosition().x - getRectangle().width / 2f,
                    getPosition().y - getRectangle().height / 1.5f);
            font.setColor(oldColor);

            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setColor(getRectColor());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }
    }

    public abstract Rectangle getRectangle();

    public Vector2 getPosition() {
        return position;
    }

    public MyGdxGame getGame() {
        return game;
    }

    public Color getRectColor() {
        return rectColor;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }
}
