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

    private final float SCALE;
    private float hp;
    private Direction preferredDirection;
    private final Vector2 speed;
    private final Rectangle tmpRect;
    private final Timer timer;
    private final Color hpBarColor;
    private final Vector2 tmpVector;

    public Slime(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, x, y, gameScreen);
        this.id = UUID.randomUUID();
        this.game = game;
        this.gameScreen = gameScreen;
        this.hp = 100;
        this.batch = game.getBatch();
        this.shapeRenderer = game.getShapeRenderer();
        this.animationFrames = new TextureRegion[6];
        for (int i = 0; i < animationFrames.length; i++) {
            TextureAtlas.AtlasRegion region = this.game.getGameResources().findRegion("slime" + (i + 1));
            this.animationFrames[i] = region;
        }
        this.animation = new Animation<>(0.2f, animationFrames);
        this.currentFrame = animation.getKeyFrames()[0];
        this.SCALE = 2f;
        this.hpBarColor = new Color(0f, 1f, 0f, 0.5f);
        this.getRectColor().set(0.1f, 0.2f, 0.8f, 0.5f);
        this.tmpRect = new Rectangle();
        this.speed = new Vector2(50, 50);
        this.preferredDirection = getRandomPreferredDirection();
        this.timer = new Timer();

        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                preferredDirection = getRandomPreferredDirection();
            }
        };
        timer.scheduleTask(task, 0, 10, -1);
        timer.start();
        this.tmpVector = new Vector2();
        this.font = game.getFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(0.75f, 0.75f);
    }

    public void render(float delta) {
        update(delta);
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
        shapeRenderer.setColor(hpBarColor);
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f,0.1f,0.1f, 0.5f);
        shapeRenderer.rect(getRectangle().x-12,
                getRectangle().y + getRectangle().height+8,
                54,
                14);
        shapeRenderer.setColor(hpBarColor);
        shapeRenderer.rect(getRectangle().x - 10,
                getRectangle().y + getRectangle().height + 10,
                hp / 2f,
                10);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        if (game.isDebug()) {
            font.draw(batch, String.valueOf((int) hp), getRectangle().x + 5,
                    getRectangle().y + getRectangle().height + 20);
        }
        super.render(delta);
    }

    private void update(float delta) {
        updateCurrentFrame();
        checkCollision(delta);
        move(delta);
    }

    private void checkCollision(float delta) {
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

    @Override
    public TextureRegion getTexture() {
        return currentFrame;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - currentFrame.getRegionWidth() * SCALE / 2f + 3,
                getPosition().y - currentFrame.getRegionHeight() * SCALE / 2f + 3,
                currentFrame.getRegionWidth() * SCALE - 6,
                currentFrame.getRegionHeight() * SCALE - 6);
    }

    public void hit(float damage) {
        this.hp -= damage;
    }

    public float getHp() {
        return hp;
    }

    public UUID getId() {
        return id;
    }
}
