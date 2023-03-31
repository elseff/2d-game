package com.elseff.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.elseff.game.MyGdxGame;

public class Body {
    private final MyGdxGame game;
    private final Vector2 position;
    private final Vector2 speed;
    private final TextureRegion textureRegion;
    private final Color color;
    private final float SCALE;

    public Body(MyGdxGame game, int x, int y, Color color) {
        this.game = game;
        this.color = color;
        this.position = new Vector2(x, y);
        this.speed = new Vector2(0.0f, 0.0f);
        this.textureRegion = new TextureRegion(new Texture("big_wooden_box.png"));
        this.SCALE = 3f;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, float dt) {
        update(dt);
        batch.begin();
        batch.draw(textureRegion,
                position.x - textureRegion.getTexture().getWidth() / 2f,
                position.y - textureRegion.getTexture().getHeight() / 2f,
                textureRegion.getTexture().getWidth() / 2f,
                textureRegion.getTexture().getHeight() / 2f,
                textureRegion.getTexture().getWidth(),
                textureRegion.getTexture().getHeight(),
                SCALE,
                SCALE,
                0.0f);
        batch.end();

        if (game.isDebug()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(position.x - textureRegion.getTexture().getWidth() * SCALE / 2f,
                    position.y - textureRegion.getTexture().getHeight() * SCALE / 2f,
                    textureRegion.getRegionWidth() * SCALE,
                    textureRegion.getRegionHeight() * SCALE);
            shapeRenderer.end();
        }
    }

    private void update(float dt) {
        move(dt);
        checkCollision(dt);
    }

    private void checkCollision(float dt) {
        if (position.x + textureRegion.getTexture().getWidth() * SCALE / 2f > game.getSCREEN_WIDTH()) {
            position.x = game.getSCREEN_WIDTH() - textureRegion.getTexture().getWidth() * SCALE / 2f;
            speed.x = 0;
        } else if (position.x - textureRegion.getTexture().getHeight() * SCALE / 2f < 0) {
            position.x = textureRegion.getRegionWidth() * SCALE / 2f;
            speed.x = 0;
        }

        if (position.y + textureRegion.getRegionHeight() * SCALE / 2f >= game.getSCREEN_HEIGHT()) {
            position.y = game.getSCREEN_HEIGHT() - textureRegion.getRegionHeight() * SCALE / 2f;
            speed.y = 0;
        } else if (position.y - textureRegion.getRegionHeight() * SCALE / 2f <= 0) {
            position.y = textureRegion.getRegionHeight() * SCALE / 2f;
            speed.y = 0;
        }
        position.x = position.x + speed.x * dt;
        position.y = position.y + speed.y * dt;
    }

    public void move(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            if (speed.x <= 360)
                speed.x += 20;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            if (speed.x >= -360)
                speed.x -= 20;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            if (speed.y <= 360)
                speed.y += 20;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            if (speed.y >= -360)
                speed.y -= 20;

        if (speed.x > 0)
            speed.x -= 10;
        if (speed.x < 0)
            speed.x += 10;
        if (speed.y > 0)
            speed.y -= 10;
        if (speed.y < 0)
            speed.y += 10;

        position.x = position.x + speed.x * dt;
        position.y = position.y + speed.y * dt;
    }
}
