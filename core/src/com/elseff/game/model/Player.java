package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.Direction;

public class Player extends GameObject {
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> rightLeftAnimation; // flip right moving sprites for left movement
    private final Animation<TextureRegion> upAnimation;

    private final TextureRegion[] downAnimationFrames;
    private final TextureRegion[] rightLeftAnimationFrames; // will flipping by horizontal
    private final TextureRegion[] upAnimationFrames;

    private TextureRegion currentFrame;

    private final SpriteBatch batch;
    private final Vector2 speed;
    private final float SCALE;
    private Direction direction;
    private final Rectangle tmpRect;

    public Player(MyGdxGame game, float x, float y) {
        super(game, x, y);
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

        this.currentFrame = downAnimation.getKeyFrames()[0];

        this.speed = new Vector2(200.0f, 200.0f);
        this.batch = getGame().getBatch();
        this.SCALE = 2f;

        this.direction = Direction.STAY;
        this.tmpRect = new Rectangle();
    }

    @Override
    public void render(float dt) {
        update(dt);
        batch.draw(currentFrame,
                getPosition().x - currentFrame.getRegionWidth() / 2f,
                getPosition().y - currentFrame.getRegionHeight() / 2f,
                currentFrame.getRegionWidth() / 2f,
                currentFrame.getRegionHeight() / 2f,
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                SCALE,
                SCALE,
                0.0f);
        super.render(dt);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - currentFrame.getRegionWidth() * SCALE / 2f+2,
                getPosition().y - currentFrame.getRegionHeight() * SCALE / 2f,
                currentFrame.getRegionWidth() * SCALE-2,
                currentFrame.getRegionHeight() * SCALE);
    }

    private void update(float dt) {
        checkMovement(dt);
        changeCurrentFrame();
    }

    private void move(Direction direction, float dt) {
        this.direction = direction;
        tmpRect.set(
                getRectangle().x + direction.getVx() * speed.x * dt,
                getRectangle().y + direction.getVy() * speed.y * dt,
                getRectangle().width,
                getRectangle().height);
        if (getGame().getScreen().getMapRenderer().getMap().isAreaClear(tmpRect)) {
            getPosition().set(getPosition().x + direction.getVx() * speed.x * dt,
                    getPosition().y + direction.getVy() * speed.y * dt);
        }
    }

    public void changeCurrentFrame() {
        switch (this.direction) {
            case STAY -> currentFrame = downAnimation.getKeyFrames()[0];
            case DOWN -> currentFrame = downAnimation.getKeyFrame(getGame().getTime(), true);
            case UP -> currentFrame = upAnimation.getKeyFrame(getGame().getTime(), true);
            case LEFT -> {
                TextureRegion frame = rightLeftAnimation.getKeyFrame(getGame().getTime(), true);
                if (!frame.isFlipX())
                    frame.flip(true, false);
                currentFrame = frame;
            }
            case RIGHT -> {
                TextureRegion frame = rightLeftAnimation.getKeyFrame(getGame().getTime(), true);
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
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            move(Direction.UP, dt);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            move(Direction.DOWN, dt);
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            move(Direction.LEFT, dt);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            move(Direction.RIGHT, dt);

        if (!(Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.S))) {
            move(Direction.STAY, dt);
        }
    }

    public void dispose() {
        for (int i = 0; i < downAnimationFrames.length; i++)
            downAnimationFrames[i].getTexture().dispose();
        for (int i = 0; i < upAnimationFrames.length; i++)
            upAnimationFrames[i].getTexture().dispose();
        for (int i = 0; i < rightLeftAnimationFrames.length; i++)
            rightLeftAnimationFrames[i].getTexture().dispose();

        this.currentFrame.getTexture().dispose();
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public float getSCALE() {
        return SCALE;
    }

    public Direction getDirection() {
        return direction;
    }
}
