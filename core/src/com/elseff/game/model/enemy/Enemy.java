package com.elseff.game.model.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;
import com.elseff.game.model.GameObject;
import com.elseff.game.model.player.Player;
import com.elseff.game.screen.GameScreen;

public abstract class Enemy extends GameObject {
    private TextureRegion textureRegion;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private Circle collideCircle;
    private boolean isSeePlayer;
    private float hp;

    protected Enemy(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
        hp = 100;
        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();
        collideCircle = new Circle(0,0,300);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPosition().x - textureRegion.getRegionWidth() * getSCALE() / 2f,
                getPosition().y - textureRegion.getRegionHeight() * getSCALE() / 2f,
                textureRegion.getRegionWidth() * getSCALE(),
                textureRegion.getRegionHeight() * getSCALE());
    }

    private void update(){
        collideCircle.x = getPosition().x;
        collideCircle.y = getPosition().y;
        updatePlayerVisibility();
    }

    private void updatePlayerVisibility() {
        Player player = getGame().getGameScreen().getPlayer();
        isSeePlayer = collideCircle.contains(player.getPosition());
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
        shapeRenderer.circle(collideCircle.x, collideCircle.y, collideCircle.radius);
        renderDebug();
        super.render(delta);
    }

    private void renderDebug() {
        if (!getGame().isDebug()) return;
        getGame().getShapeRenderer().setColor(new Color(1f, 1f, 1f, 0.5f));
        //CIRCLES
        int countCircles = 25;
        float x1 = getPosition().x;
        Vector2 playerPos = getGame().getGameScreen().getPlayer().getPosition();
        float x2 = playerPos.x;
        float y1 = getPosition().y;
        float y2 = playerPos.y;
        int radius = 10;
        for (int j = 0; j < countCircles; j++) {
            float x = x1 + (x2 - x1) / countCircles * j;
            float y = y1 + (y2 - y1) / countCircles * j;
            shapeRenderer.circle(x, y, radius);
        }
    }

    public abstract void hit(float damage);

    public abstract void regenerationHp(float value);

    public abstract TextureRegion getTexture();

    public abstract float getSCALE();

    public float getHp() {
        return hp;
    }

    public boolean isSeePlayer(){
        return isSeePlayer;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }
}
