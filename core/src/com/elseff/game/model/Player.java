package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.Direction;
import com.elseff.game.screen.GameScreen;

public class Player {
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final Vector2 position;
    private final Vector2 speed;
    private TextureRegion currentFrame;
    private final Animation<TextureRegion> animation;
    private final TextureRegion[] animationFrames;
    private final Color color;
    private final float SCALE;
    private final Color blueColor;
    private final Rectangle physicRectangle;
    private final int physicRectangleOpacity;
    private Direction direction;

    public Player(MyGdxGame game, GameScreen gameScreen, int x, int y) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.position = new Vector2(x, y);
        this.speed = new Vector2(400.0f, 400.0f);

        this.SCALE = 5f;
        animationFrames = game.getGameResources().findRegions();
        animation = new Animation<>(0.1f, animationFrames);

        this.physicRectangleOpacity = 50;
        this.blueColor = new Color(0, 191, 255, physicRectangleOpacity / 100f);
        this.color = Color.WHITE;
        currentFrame = animation.getKeyFrame(game.getTime(), true);

        this.physicRectangle = new Rectangle();
        this.physicRectangle.x = position.x - currentFrame.getRegionWidth() * SCALE / 2f - 5;
        this.physicRectangle.y = position.y - currentFrame.getRegionHeight() * SCALE / 2f - 5;
        this.physicRectangle.width = currentFrame.getRegionWidth() * SCALE + 10;
        this.physicRectangle.height = currentFrame.getRegionHeight() * SCALE + 10;
        direction = Direction.STAY;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, float dt) {
        update(dt);
        if (game.isDebug()) {
            shapeRenderer.setColor(color);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(position.x - currentFrame.getRegionWidth() * SCALE / 2f,
                    position.y - currentFrame.getRegionHeight() * SCALE / 2f,
                    currentFrame.getRegionWidth() * SCALE,
                    currentFrame.getRegionHeight() * SCALE);
            shapeRenderer.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(blueColor);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(physicRectangle.x,
                    physicRectangle.y,
                    physicRectangle.width,
                    physicRectangle.height);
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        batch.begin();
        batch.draw(currentFrame,
                position.x - currentFrame.getRegionWidth() / 2f,
                position.y - currentFrame.getRegionHeight() / 2f,
                currentFrame.getRegionWidth() / 2f,
                currentFrame.getRegionHeight() / 2f,
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                SCALE,
                SCALE,
                0.0f);
        if (game.isDebug()) {
            font.draw(batch,
                    String.format("(%.1f; %.1f)", position.x, position.y),
                    position.x - currentFrame.getRegionWidth() * SCALE / 4f,
                    position.y);
        }
        batch.end();
    }

    private void update(float dt) {
        if (direction == Direction.STAY)
            currentFrame = animation.getKeyFrames()[0];
        else
            currentFrame = animation.getKeyFrame(game.getTime(), true);

        checkMovement(dt);
        checkCollision(dt);
    }

    private void checkCollision(float dt) {
        checkBorderCollision(dt);
    }

    private void checkBorderCollision(float dt) {
        if (position.x + currentFrame.getRegionHeight() * SCALE / 2f > game.getSCREEN_WIDTH()) {
            position.x = game.getSCREEN_WIDTH() - currentFrame.getRegionHeight() * SCALE / 2f;
        } else if (position.x - currentFrame.getRegionHeight() * SCALE / 2f < 0) {
            position.x = currentFrame.getRegionWidth() * SCALE / 2f;
        }

        if (position.y + currentFrame.getRegionHeight() * SCALE / 2f >= game.getSCREEN_HEIGHT()) {
            position.y = game.getSCREEN_HEIGHT() - currentFrame.getRegionHeight() * SCALE / 2f;
        } else if (position.y - currentFrame.getRegionHeight() * SCALE / 2f <= 0) {
            position.y = currentFrame.getRegionHeight() * SCALE / 2f;
        }
    }

    private void move(Direction direction, float dt) {
        this.direction = direction;
        position.add(direction.getVx() * speed.x * dt,
                direction.getVy() * speed.y * dt);
        physicRectangle.x = position.x - currentFrame.getRegionWidth() * SCALE / 2f - 5;
        physicRectangle.y = position.y - currentFrame.getRegionHeight() * SCALE / 2f - 5;
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (currentFrame.isFlipX())
                currentFrame.flip(true,false);
            move(Direction.RIGHT, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!currentFrame.isFlipX())
                currentFrame.flip(true, false);
            move(Direction.LEFT, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            move(Direction.UP, dt);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            move(Direction.DOWN, dt);
        if (!(Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.S))) {
            move(Direction.STAY, dt);
        }
    }

    public Rectangle getPhysicRectangle() {
        return physicRectangle;
    }
}
