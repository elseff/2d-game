package com.elseff.game.model.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.misc.Direction;
import com.elseff.game.misc.popupmsg.PopUpMessage;
import com.elseff.game.misc.popupmsg.PopUpMessageType;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.enemy.Enemy;
import com.elseff.game.model.food.Food;
import com.elseff.game.screen.GameScreen;
import com.elseff.game.util.MathUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Rectangle rectangle;
    private final Rectangle chunkGeneratorRectangle;
    private final Color chunkGeneratorRectangleColor;

    private float hp;

    private boolean isCollidingWithMonster;

    private final Timer shadowParticlesGenerationTimer;
    private final Timer shadowParticlesDegenerationTimer;

    private Map<Vector2, Float> particlesPositions;

    private final Color defaultRectColor;
    private final Color hittingByEnemyColor;

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

        defaultSpeed = new Vector2(300f, 300f);
        speed = new Vector2();
        speed.set(defaultSpeed);

        SCALE = 2f;
        tmpRect = new Rectangle();

        direction = Direction.IDLE;
        reversedDirection = new Vector2(direction.getVx(), direction.getVy());

        chunkGeneratorRectangle = new Rectangle();
        chunkGeneratorRectangleColor = Color.YELLOW;
        chunkGeneratorRectangle.width = game.getSCREEN_WIDTH();
        chunkGeneratorRectangle.height = game.getSCREEN_HEIGHT();

        hp = 100f;
        isCollidingWithMonster = false;


        particlesPositions = new HashMap<>();
        shadowParticlesGenerationTimer = new Timer();
        shadowParticlesDegenerationTimer = new Timer();

        Timer.Task generationTask = new Timer.Task() {
            @Override
            public void run() {
                if (!direction.equals(Direction.IDLE)) {
                    Vector2 playerPos = getPosition();
                    Vector2 vector2 = new Vector2(playerPos.x - (direction.getVx() * 15), playerPos.y - (direction.getVy() * 15));
                    particlesPositions.put(vector2, 0.1f);
                }
            }
        };

        Timer.Task degenerationTask = new Timer.Task() {
            @Override
            public void run() {
                particlesPositions = particlesPositions.entrySet()
                        .stream()
                        .filter(e -> e.getValue() > 0)
                        .peek(e -> e.setValue(e.getValue() - 0.01f))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
        };

        shadowParticlesGenerationTimer.scheduleTask(generationTask, 0, 0.2f, -1);
        shadowParticlesGenerationTimer.start();
        shadowParticlesDegenerationTimer.scheduleTask(degenerationTask, 0, 0.3f, -1);
        shadowParticlesDegenerationTimer.start();
        defaultRectColor = new Color(0.8f, 0.7f, 0.1f, 0.5f);
        hittingByEnemyColor = new Color(1, 0, 0, 0.5f);
        getRectColor().set(defaultRectColor);
        rectangle = new Rectangle();
    }

    @Override
    public Rectangle getRectangle() {
        rectangle.set(getPosition().x - currentFrame.getRegionWidth() * SCALE / 2f + 2,
                getPosition().y - currentFrame.getRegionHeight() * SCALE / 2f,
                currentFrame.getRegionWidth() * SCALE - 2,
                currentFrame.getRegionHeight() * SCALE);

        return rectangle;
    }

    @Override
    public void render(float dt) {
        update(dt);

        renderParticles();

        super.render(dt);

        if (isCollidingWithMonster)
            batch.setShader(getGame().getRedShader());

        if (!batch.isDrawing())
            batch.begin();

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

        Gdx.gl.glEnable(GL20.GL_BLEND);

        renderDebug();
    }

    private void renderParticles() {
        getGame().GRACEFUL_SHAPE_RENDERER_BEGIN();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        for (Map.Entry<Vector2, Float> e : particlesPositions.entrySet()) {
            Vector2 pos = e.getKey();
            Float lifetime = e.getValue();
            Color color = new Color(0, 0, 0, lifetime);
            shapeRenderer.setColor(color);
            shapeRenderer.circle(pos.x, pos.y, 20, 12);
        }
        getGame().GRACEFUL_SHAPE_RENDERER_END();
    }

    private void renderDebug() {
        if (!getGame().isDebug()) return;

        getGame().GRACEFUL_SHAPE_RENDERER_BEGIN();
        shapeRenderer.setColor(chunkGeneratorRectangleColor);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(chunkGeneratorRectangle.x,
                chunkGeneratorRectangle.y,
                chunkGeneratorRectangle.width,
                chunkGeneratorRectangle.height);

        shapeRenderer.setColor(new Color(1,0,0,0.5f));
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(0,0,300);
        getGame().GRACEFUL_SHAPE_RENDERER_END();
    }

    private void update(float dt) {
        if (MathUtils.isCircleOverlapsWithRectangle(new Circle(0,0,300), getRectangle())){
            System.out.println("DICK");
        }
        checkCollision(dt);
        checkMovement(dt);
        changeCurrentFrame();
        updateChunkGeneratorRectangle();
        regenerationHp(dt * 2);
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
            case IDLE -> currentFrame = downAnimation.getKeyFrames()[0];
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
            move(Direction.IDLE, dt);
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(Direction.IDLE, dt);
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
            move(Direction.IDLE, dt);
        }
    }

    private void checkCollision(float delta) {
        Set<Chunk> currentChunks = getGameScreen().getMap().getCurrentChunks();
        currentChunks.forEach(currentChunk -> {
                    Array<Enemy> enemies = getGameScreen().getMap().getEnemies();
                    isCollidingWithMonster = false;
                    for (int i = 0; i < enemies.size; i++) {
                        Enemy enemy = enemies.get(i);
                        if (getRectangle().overlaps(enemy.getRectangle())) {
                            hit((float) (Math.random()));
                            enemy.hit((float) (Math.random() * 5));
                            isCollidingWithMonster = true;
                            speed.set(defaultSpeed.x / 2f, defaultSpeed.y / 2f);
                            boolean listHasMessageWithTypePlayerHit = false;
                            for (int j = 0; j < getGameScreen().getPopUpMessagesController().getMessages().size; j++) {
                                PopUpMessage message = getGameScreen().getPopUpMessagesController().getMessages().get(j);
                                if (PopUpMessageType.PLAYER_HIT.equals(message.getType())) {
                                    listHasMessageWithTypePlayerHit = true;
                                    break;
                                }
                            }
                            if (!listHasMessageWithTypePlayerHit) {
                                PopUpMessage message = new PopUpMessage(
                                        "HIT",
                                        getPosition().x - 10,
                                        getPosition().y + 40,
                                        Color.RED,
                                        PopUpMessageType.PLAYER_HIT);
                                getGameScreen().getPopUpMessagesController().addMessage(
                                        message
                                );
                                Gdx.app.log("PLAYER", message.getText() + " - " + message.getPosition());
                            }
                            break;
                        } else {
                            speed.set(defaultSpeed);
                        }
                    }
                    for (int i = 0; i < currentChunk.getFoodArray().size; i++) {
                        Food food = currentChunk.getFoodArray().get(i);
                        if (getRectangle().overlaps(food.getRectangle())) {
                            currentChunk.getFoodArray().removeValue(food, true);
                            hit(-15);

                            boolean listHasMessageWithTypePlayerHealth = false;
                            for (int j = 0; j < getGameScreen().getPopUpMessagesController().getMessages().size; j++) {
                                PopUpMessage message = getGameScreen().getPopUpMessagesController().getMessages().get(j);
                                if (PopUpMessageType.PLAYER_HEALTH.equals(message.getType())) {
                                    listHasMessageWithTypePlayerHealth = true;
                                    break;
                                }
                            }
                            if (!listHasMessageWithTypePlayerHealth) {
                                PopUpMessage message = new PopUpMessage(
                                        "HEALTH",
                                        getPosition().x - 40,
                                        getPosition().y + 40,
                                        new Color(0.2f, 0.8f, 0.2f, 1.0f),
                                        PopUpMessageType.PLAYER_HEALTH);
                                getGameScreen().getPopUpMessagesController().addMessage(
                                        message
                                );
                                Gdx.app.log("PLAYER", message.getText() + " - " + message.getPosition());
                            }
                        }
                    }
                }
        );
    }

    private void updateRectColor() {
        if (isCollidingWithMonster)
            getRectColor().set(hittingByEnemyColor);
        else
            getRectColor().set(defaultRectColor);
    }

    public void hit(float damage) {
        this.hp -= damage;
    }

    private void regenerationHp(float value) {
        if (hp + value <= 100)
            hp += value;
        else
            hp = 100;
    }

    public void dispose() {
        for (TextureRegion downAnimationFrame : downAnimationFrames) downAnimationFrame.getTexture().dispose();
        for (TextureRegion upAnimationFrame : upAnimationFrames) upAnimationFrame.getTexture().dispose();
        for (TextureRegion rightLeftAnimationFrame : rightLeftAnimationFrames)
            rightLeftAnimationFrame.getTexture().dispose();

        currentFrame.getTexture().dispose();
    }

    public void changeSpeed(float value) {
        defaultSpeed.add(value, value);
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

    public Map<Vector2, Float> getParticlesPositions() {
        return particlesPositions;
    }
}
