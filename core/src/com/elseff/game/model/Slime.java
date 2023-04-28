package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.Direction;
import com.elseff.game.screen.GameScreen;

import java.util.UUID;

public class Slime extends Enemy {
    private final UUID id;
    private final MyGdxGame game;
    private final GameScreen gameScreen;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private final TextureRegion[] animationFrames;
    private final Animation<TextureRegion> animation;
    private TextureRegion currentFrame;

    private Direction preferredDirection;
    private final Vector2 speed;
    private final Rectangle tmpRect;
    private final Timer timer;
    private final Color hpBarColor;
    private final Vector2 tmpVector;

    public Slime(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, x, y, gameScreen);
        id = UUID.randomUUID();
        this.game = game;
        this.gameScreen = gameScreen;

        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();

        animationFrames = new TextureRegion[6];

        for (int i = 0; i < animationFrames.length; i++) {
            TextureAtlas.AtlasRegion region = this.game.getGameResources().findRegion("slime" + (i + 1));
            animationFrames[i] = region;
        }
        animation = new Animation<>(0.2f, animationFrames);
        currentFrame = animation.getKeyFrames()[0];

        preferredDirection = getRandomPreferredDirection();

        hpBarColor = new Color(0f, 1f, 0f, 0.5f);
        getRectColor().set(1, 0.5f, 0, 0.5f);

        speed = new Vector2(50, 50);

        timer = new Timer();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                preferredDirection = getRandomPreferredDirection();
            }
        };
        timer.scheduleTask(task, 0, 10, -1);
        timer.start();

        tmpRect = new Rectangle();
        tmpVector = new Vector2();

        font = game.getFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(0.75f, 0.75f);
    }

    @Override
    public float getSCALE() {
        return 2.0f;
    }

    @Override
    public TextureRegion getTexture() {
        return currentFrame;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - currentFrame.getRegionWidth() * getSCALE() / 2f + 3,
                getPosition().y - currentFrame.getRegionHeight() * getSCALE() / 2f + 3,
                currentFrame.getRegionWidth() * getSCALE() - 6,
                currentFrame.getRegionHeight() * getSCALE() - 6);
    }

    @Override
    public void hit(float damage) {
        setHp(getHp()-damage);
    }

    private void update(float delta) {
        updateCurrentFrame();
        move(delta);
    }

    public void render(float delta) {
        update(delta);
        super.render(delta);
        batch.end();

        renderHpBar();

        batch.begin();
        if (game.isDebug()) {
            font.draw(batch, String.valueOf((int) getHp()), getRectangle().x + 5,
                    getRectangle().y + getRectangle().height + 20);
        }
        super.render(delta);
    }

    private void renderHpBar() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setColor(0.1f,0.1f,0.1f, 0.5f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //border rectangle of hp bar
        shapeRenderer.rect(getRectangle().x-12,
                getRectangle().y + getRectangle().height+8,
                54,
                14);

        shapeRenderer.setColor(hpBarColor);

        //hp bar
        shapeRenderer.rect(getRectangle().x - 10,
                getRectangle().y + getRectangle().height + 10,
                getHp() / 2f,
                10);

        shapeRenderer.end();
    }

    private Direction getReversedDirection(){
        return switch (preferredDirection){
            case STAY -> Direction.STAY;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
        };
    }

    private void move(float dt) {
        tmpRect.set(
                getRectangle().x + preferredDirection.getVx() * speed.x * dt,
                getRectangle().y + preferredDirection.getVy() * speed.y * dt,
                getRectangle().width,
                getRectangle().height);
        if (getGameScreen().getMap().isAreaClear(tmpRect)) {
            getPosition().set(getPosition().x + preferredDirection.getVx() * speed.x * dt,
                    getPosition().y + preferredDirection.getVy() * speed.y * dt);
        } else {
            preferredDirection = getRandomPreferredDirection();
        }
    }

    private Direction getRandomPreferredDirection() {
        int random = (int) (Math.random() * 5);
        return switch (random) {
            case 0 -> Direction.STAY;
            case 1 -> Direction.UP;
            case 2 -> Direction.LEFT;
            case 3 -> Direction.RIGHT;
            case 4 -> Direction.DOWN;
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }

    private void updateCurrentFrame() {
        this.currentFrame = animation.getKeyFrame(getGame().getTime(), true);
    }

    public UUID getId() {
        return id;
    }
}
