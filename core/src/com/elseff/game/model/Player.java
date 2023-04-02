package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.Direction;

public class Player {
    private final MyGdxGame game;
    private final Vector2 position;
    private final Vector2 speed;

    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> rightLeftAnimation; // flip right moving sprites for left movement
    private final Animation<TextureRegion> upAnimation;

    private final TextureRegion[] downAnimationFrames;
    private final TextureRegion[] rightLeftAnimationFrames; // will flipping by horizontal
    private final TextureRegion[] upAnimationFrames;

    private TextureRegion currentFrame;

    private final float SCALE;
    private Direction direction;

    public Player(MyGdxGame game, int x, int y) {
        this.game = game;
        this.position = new Vector2(x, y);
        this.speed = new Vector2(400.0f, 400.0f);

        this.downAnimationFrames = new TextureRegion[10]; // 10 frames for down animation
        this.rightLeftAnimationFrames = new TextureRegion[8]; // 8 frames for right/left animation
        this.upAnimationFrames = new TextureRegion[10]; // 10 frames for up animation

        for (int i = 0; i < 10; i++)
            downAnimationFrames[i] = game.getGameResources().findRegion("ninja" + i);
        for (int i = 0; i < 8; i++)
            rightLeftAnimationFrames[i] = game.getGameResources().findRegion("ninja_right" + i);
        for (int i = 0; i < 10; i++)
            upAnimationFrames[i] = game.getGameResources().findRegion("ninja_up" + i);

        this.downAnimation = new Animation<>(0.08f, downAnimationFrames);
        this.rightLeftAnimation = new Animation<>(0.1f, rightLeftAnimationFrames);
        this.upAnimation = new Animation<>(0.08f, upAnimationFrames);

        currentFrame = downAnimation.getKeyFrames()[0];
        this.SCALE = 5f;
        this.direction = Direction.STAY;
    }

    public void render(SpriteBatch batch, BitmapFont font, float dt) {
        update(dt);

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
                    position.x - currentFrame.getRegionWidth() * SCALE / 2f,
                    position.y + currentFrame.getRegionWidth() * SCALE / 1.5f);
        }
        batch.end();
    }

    private void update(float dt) {
        checkMovement(dt);
        changeCurrentFrame();
    }

    private void move(Direction direction, float dt) {
        this.direction = direction;
        position.add(direction.getVx() * speed.x * dt,
                direction.getVy() * speed.y * dt);
    }

    public void changeCurrentFrame() {
        switch (this.direction) {
            case STAY -> currentFrame = downAnimation.getKeyFrames()[0];
            case DOWN -> currentFrame = downAnimation.getKeyFrame(game.getTime(), true);
            case UP -> currentFrame = upAnimation.getKeyFrame(game.getTime(), true);
            case LEFT -> {
                TextureRegion frame = rightLeftAnimation.getKeyFrame(game.getTime(), true);
                if (!frame.isFlipX())
                    frame.flip(true, false);
                currentFrame = frame;
            }
            case RIGHT -> {
                TextureRegion frame = rightLeftAnimation.getKeyFrame(game.getTime(), true);
                if (frame.isFlipX())
                    frame.flip(true, false);
                currentFrame = frame;
            }
        }
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(Direction.STAY, dt);
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(Direction.STAY, dt);
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            move(Direction.UP, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(Direction.DOWN, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(Direction.LEFT, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(Direction.RIGHT, dt);
        }

        if (!(Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.S))) {
            move(Direction.STAY, dt);
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}
