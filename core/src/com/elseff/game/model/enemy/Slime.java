package com.elseff.game.model.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.elseff.game.MyGdxGame;
import com.elseff.game.misc.Direction;
import com.elseff.game.model.player.Player;
import com.elseff.game.screen.GameScreen;


public class Slime extends Enemy {
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
    private final Rectangle rectangle;
    private final Timer timer;
    private final Color hpBarColor;
    private final Vector2 tmpVector;

    public Slime(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, x, y, gameScreen);
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

        speed = new Vector2(100, 100);

        timer = new Timer();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                if (EnemyState.IDLE.equals(getState()))
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
        rectangle = new Rectangle();
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
        rectangle.set(getPosition().x - currentFrame.getRegionWidth() * getSCALE() / 2f + 3,
                getPosition().y - currentFrame.getRegionHeight() * getSCALE() / 2f + 3,
                currentFrame.getRegionWidth() * getSCALE() - 6,
                currentFrame.getRegionHeight() * getSCALE() - 6);

        return rectangle;
    }

    @Override
    public void hit(float damage) {
        setHp(getHp() - damage);
        updateHpBarColor();
    }

    @Override
    public void regenerationHp(float value) {
        if (getHp() + value <= 100)
            setHp(getHp() + value);
        else
            setHp(100);

        updateHpBarColor();
    }

    private void update(float delta) {
        updateCurrentFrame();
        regenerationHp(delta / 2);
        if (EnemyState.IDLE.equals(getState()))
            move(delta);

        attack(delta);
    }

    private void moveToPoint(float delta, Vector2 position) {
        tmpVector.set(position).sub(getPosition());
        tmpVector.set(tmpVector.nor());
        tmpRect.set(
                getRectangle().x + tmpVector.x * speed.x * delta,
                getRectangle().y + tmpVector.y * speed.y * delta,
                getRectangle().width,
                getRectangle().height);
        if (getGameScreen().getMap().isAreaClear(tmpRect)) {
            getPosition().set(getPosition().x + tmpVector.x * speed.x * delta,
                    getPosition().y + tmpVector.y * speed.y * delta);
        }
    }

    private void attack(float delta) {
        Player player = game.getGameScreen().getPlayer();
        Vector2 playerPosition = player.getPosition();

        if (EnemyState.SEES_PLAYER.equals(getState()))
            moveToPoint(delta, player.getPosition());
    }

    private void updateHpBarColor() {
        if (getHp() >= 70)
            hpBarColor.set(0f, 1f, 0f, 0.5f);
        else if (getHp() >= 30)
            hpBarColor.set(1, 1, 0, 0.5f);
        else
            hpBarColor.set(1, 0, 0, 0.5f);
    }

    public void render(float delta) {
        update(delta);
        super.render(delta);
        renderHpBar();
        renderDebug();
    }

    private void renderDebug() {
        if (!game.isDebug()) return;

        game.GRACEFUL_SHAPE_RENDERER_END();
        // HP
        font.draw(batch, String.valueOf((int) getHp()), getRectangle().x + 5,
                getRectangle().y + getRectangle().height + 20);

        game.GRACEFUL_SHAPE_RENDERER_BEGIN(ShapeRenderer.ShapeType.Line);
    }

    private void renderHpBar() {
        game.GRACEFUL_SHAPE_RENDERER_BEGIN(ShapeRenderer.ShapeType.Filled);

        //border rectangle of hp bar
        shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
        shapeRenderer.rect(getRectangle().x - 12,
                getRectangle().y + getRectangle().height + 8,
                54,
                14);

        //hp bar
        shapeRenderer.setColor(hpBarColor);
        shapeRenderer.rect(getRectangle().x - 10,
                getRectangle().y + getRectangle().height + 10,
                getHp() / 2f,
                10);

        game.GRACEFUL_SHAPE_RENDERER_END();
    }

    private Direction getReversedDirection() {
        return switch (preferredDirection) {
            case IDLE -> Direction.IDLE;
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
            case 0 -> Direction.IDLE;
            case 1 -> Direction.UP;
            case 2 -> Direction.LEFT;
            case 3 -> Direction.RIGHT;
            case 4 -> Direction.DOWN;
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }

    private void updateCurrentFrame() {
        currentFrame = animation.getKeyFrame(getGame().getTime(), true);
    }
}
