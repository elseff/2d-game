package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.misc.Direction;
import com.elseff.game.screen.GameScreen;

public class Player extends GameObject {
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> rightLeftAnimation; // flip right moving sprites for left movement
    private final Animation<TextureRegion> upAnimation;

    private final TextureRegion[] downAnimationFrames;
    private final TextureRegion[] rightLeftAnimationFrames; // will flipping by horizontal
    private final TextureRegion[] upAnimationFrames;

    private TextureRegion currentFrame;

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    private final Vector2 speed;
    private final Vector2 defaultSpeed;

    private Direction direction;
    private final Vector2 reversedDirection;

    private final float SCALE;

    private final Rectangle tmpRect;

    private final Rectangle chunkGeneratorRectangle;
    private final Color chunkGeneratorRectangleColor;

    private float hp;

    private boolean isCollidingWithMonster;

    private final ShaderProgram shader;

    public Player(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, x, y, gameScreen);
        downAnimationFrames = new TextureRegion[10]; // 10 frames for down animation
        rightLeftAnimationFrames = new TextureRegion[8]; // 8 frames for right/left animation
        upAnimationFrames = new TextureRegion[10]; // 10 frames for up animation

        for (int i = 0; i < 10; i++)
            downAnimationFrames[i] = game.getGameResources().findRegion("ninja" + i);
        for (int i = 0; i < 8; i++)
            rightLeftAnimationFrames[i] = game.getGameResources().findRegion("ninja_right" + i);
        for (int i = 0; i < 10; i++)
            upAnimationFrames[i] = game.getGameResources().findRegion("ninja_up" + i);

        downAnimation = new Animation<>(0.07f, downAnimationFrames);
        rightLeftAnimation = new Animation<>(0.08f, rightLeftAnimationFrames);
        upAnimation = new Animation<>(0.07f, upAnimationFrames);

        currentFrame = downAnimation.getKeyFrames()[0];

        batch = getGame().getBatch();
        shapeRenderer = game.getShapeRenderer();

        defaultSpeed = new Vector2(200f, 200f);
        speed = new Vector2();
        speed.set(defaultSpeed);

        SCALE = 1.4f;
        tmpRect = new Rectangle();

        direction = Direction.STAY;
        reversedDirection = new Vector2(direction.getVx(), direction.getVy());

        chunkGeneratorRectangle = new Rectangle();
        chunkGeneratorRectangleColor = Color.BLUE;
        chunkGeneratorRectangle.width = 1920;
        chunkGeneratorRectangle.height = 1080;

        getRectColor().set(0.8f, 0.7f, 0.1f, 0.8f);

        hp = 100f;
        isCollidingWithMonster = false;

//        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert"),
                Gdx.files.internal("shaders/red.frag"));
    }

    @Override
    public void render(float dt) {
        update(dt);
        super.render(dt);

        if (isCollidingWithMonster)
            batch.setShader(shader);

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

        batch.setShader(null); // disable shader

        if (getGame().isDebug()) {
            batch.end();
            shapeRenderer.setColor(chunkGeneratorRectangleColor);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(chunkGeneratorRectangle.x,
                    chunkGeneratorRectangle.y,
                    chunkGeneratorRectangle.width,
                    chunkGeneratorRectangle.height);
            shapeRenderer.end();
            batch.begin();
        }
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - currentFrame.getRegionWidth() * SCALE / 2f + 2,
                getPosition().y - currentFrame.getRegionHeight() * SCALE / 2f,
                currentFrame.getRegionWidth() * SCALE - 2,
                currentFrame.getRegionHeight() * SCALE);
    }

    private void update(float dt) {
        checkCollision(dt);
        checkMovement(dt);
        changeCurrentFrame();
        updateChunkGeneratorRectangle();
        regenerationHp(dt);
        updateRectColor();
    }

    private void updateChunkGeneratorRectangle() {
        chunkGeneratorRectangle.setPosition(
                getPosition().x - chunkGeneratorRectangle.width / 2,
                getPosition().y - chunkGeneratorRectangle.height / 2);
    }

    private void move(Direction direction, float dt) {
        this.direction = direction;
        tmpRect.set(
                getRectangle().x + direction.getVx() * speed.x * dt,
                getRectangle().y + direction.getVy() * speed.y * dt,
                getRectangle().width,
                getRectangle().height);
        if (getGameScreen().getMap().isAreaClear(tmpRect))
            getPosition().set(getPosition().x + direction.getVx() * speed.x * dt,
                    getPosition().y + direction.getVy() * speed.y * dt);
    }

    private void changeCurrentFrame() {
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

    private void checkMovement(float dt) {
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

    public void checkCollision(float delta) {
        Chunk currentChunk = getGameScreen().getMap().getCurrentChunk();
        Array<Enemy> enemies = getGameScreen().getMap().getEnemies();
        isCollidingWithMonster = false;
        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if (getRectangle().overlaps(enemy.getRectangle())) {
                hit((float) (Math.random()));
                enemy.hit((float) (Math.random() * 5));
                isCollidingWithMonster = true;
                speed.set(defaultSpeed.x / 2f, defaultSpeed.y / 2f);
                break;
            } else {
                speed.set(defaultSpeed);
            }
        }
    }

    private void updateRectColor() {
        if (isCollidingWithMonster)
            getRectColor().set(1, 0, 0, 0.5f);
        else
            getRectColor().set(0.8f, 0.7f, 0.1f, 0.8f);
    }

    public void hit(float damage) {
        this.hp -= damage;
    }

    private void regenerationHp(float dt) {
        if (hp < 100)
            hp += dt * 2;
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

    public Rectangle getChunkGeneratorRectangle() {
        return chunkGeneratorRectangle;
    }

    public float getHp() {
        return hp;
    }

    public Vector2 getReversedDirection() {
        return reversedDirection;
    }

}
