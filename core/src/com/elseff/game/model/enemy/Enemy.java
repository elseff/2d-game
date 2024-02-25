package com.elseff.game.model.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.map.chunk.Chunk;
import com.elseff.game.misc.font.FontDefinition;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.Obstacle;
import com.elseff.game.model.player.Player;
import com.elseff.game.screen.GameScreen;
import com.elseff.game.util.MathUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class Enemy extends GameObject {
    private TextureRegion textureRegion;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final Circle collideCircle;
    private EnemyState state;
    private float hp;
    private final Color circlesToPlayerColor;
    private Color collideCircleColor;
    private final int circlesToPlayerRadius;
    private final Rectangle rectangle;
    private final Set<Circle> circlesToPlayer;

    protected Enemy(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
        hp = 100;
        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();
        collideCircle = new Circle(0, 0, 200);
        circlesToPlayerColor = new Color(1, 1, 1, 0.5f);
        Color rectColor = new Color(1, 0.2f, 0, 0.4f);
        getRectColor().set(rectColor);
        state = EnemyState.IDLE;
        circlesToPlayerRadius = 10;
        rectangle = new Rectangle();
        circlesToPlayer = new HashSet<>();
    }

    @Override
    public Rectangle getRectangle() {
        rectangle.set(getPosition().x - textureRegion.getRegionWidth() * getSCALE() / 2f,
                getPosition().y - textureRegion.getRegionHeight() * getSCALE() / 2f,
                textureRegion.getRegionWidth() * getSCALE(),
                textureRegion.getRegionHeight() * getSCALE());

        return rectangle;
    }

    private void update() {
        updateCollideCirclePosition();
        updateEnemyState();
        updateEnemyState();
        updateCirclesToPlayer();
    }

    private void updateCirclesToPlayer() {
        circlesToPlayer.clear();

        Vector2 playerPos = getGame().getGameScreen().getPlayer().getPosition();
        double distance = MathUtils.distanceBetweenTwoPoints(playerPos, getPosition());
        float x1 = playerPos.x;
        float x2 = getPosition().x;
        float y1 = playerPos.y;
        float y2 = getPosition().y;
        double countCircles = distance / (circlesToPlayerRadius * 2);
        //        for (int j = 0; j < countCircles; j++) {
//            float x = x1 + (x2 - x1) / countCircles * j;
//            float y = y1 + (y2 - y1) / countCircles * j;
//            shapeRenderer.circle(x, y, radius);
//        }
        for (int j = 0; j < countCircles; j++) {
            double x = x1 + (x2 - x1) / countCircles * j;
            double y = y1 + (y2 - y1) / countCircles * j;
            Circle circle = new Circle((float) x, (float) y, circlesToPlayerRadius);
            circlesToPlayer.add(circle);
        }
    }

    private void updateCollideCirclePosition() {
        collideCircle.x = getPosition().x;
        collideCircle.y = getPosition().y;
    }

    private void updateEnemyState() {
        Player player = getGame().getGameScreen().getPlayer();
        boolean collideCircleOverlapsWithPlayer = MathUtils.isCircleOverlapsWithRectangle(collideCircle, player.getRectangle());
        if (collideCircleOverlapsWithPlayer)
            state = isCircleToPlayerCollidesWithObstacle() ? EnemyState.LOST_PLAYER : EnemyState.SEES_PLAYER;
        else
            state = EnemyState.IDLE;
        collideCircleColor = collideCircleOverlapsWithPlayer ? Color.RED : Color.GREEN;
    }

    private boolean isCircleToPlayerCollidesWithObstacle() {
        for (Circle circle : circlesToPlayer) // for every circles to player
            for (Chunk chunk : getGameScreen().getMap().getCurrentChunks()) // for every current chunks
                for (GameObject gameObject : chunk.getObjects()) // for every gameobject in curreny chunk
                    if (gameObject instanceof Obstacle)
                        if (MathUtils.isCircleOverlapsWithRectangle(circle, gameObject.getRectangle()))
                            return true;
        return false;
    }

    public void render(float delta) {
        update();
        getGame().GRACEFUL_SHAPE_RENDERER_END();
        batch.draw(getTexture(),
                getPosition().x - getTexture().getRegionWidth() / 2f,
                getPosition().y - getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth() / 2f,
                getTexture().getRegionHeight() / 2f,
                getTexture().getRegionWidth(),
                getTexture().getRegionHeight(),
                getSCALE(),
                getSCALE(),
                0.0f);
        getGame().GRACEFUL_SHAPE_RENDERER_BEGIN();
        renderDebug();
        super.render(delta);
    }

    private void renderDebug() {
        if (!getGame().isDebug()) return;

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(getCollideCircleColor());
        shapeRenderer.circle(collideCircle.x, collideCircle.y, collideCircle.radius);

        shapeRenderer.setColor(circlesToPlayerColor);
        circlesToPlayer.forEach(circle -> shapeRenderer.circle(circle.x, circle.y, circle.radius));

        getGame().GRACEFUL_SHAPE_RENDERER_END();
        BitmapFont font = getGame().getGameResources().getFontFromDef(FontDefinition.ARIAL_20);
        font.setColor(Color.WHITE);
        font.draw(batch,
                getState().getName(),
                getPosition().x - getState().getName().length() * 3f,
                getPosition().y + 50);
        getGame().GRACEFUL_SHAPE_RENDERER_BEGIN();
    }

    private Color getCollideCircleColor() {
        return collideCircleColor;
    }

    public abstract void hit(float damage);

    public abstract void regenerationHp(float value);

    public abstract TextureRegion getTexture();

    public abstract float getSCALE();

    public float getHp() {
        return hp;
    }

    public EnemyState getState() {
        return state;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }
}
