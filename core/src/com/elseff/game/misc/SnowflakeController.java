package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public class SnowflakeController {
    private final Array<Snowflake> particles;
    private final Timer particleGenerationTimer;
    private final ShapeRenderer shapeRenderer;

    public SnowflakeController(MyGdxGame game, GameScreen gameScreen) {
        particles = new Array<>();
        particleGenerationTimer = new Timer();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                Rectangle playerChunkGeneratorRectangle = gameScreen.getPlayer().getChunkGeneratorRectangle();
                //225 degrees
                Vector2 position1 = new Vector2(
                        gameScreen.getCamera().position.x - playerChunkGeneratorRectangle.width / 2-100,
                        gameScreen.getCamera().position.y - playerChunkGeneratorRectangle.height / 2-100);
                //135 degrees
                Vector2 position2 = new Vector2(
                        gameScreen.getCamera().position.x - playerChunkGeneratorRectangle.width / 2 - 100,
                        gameScreen.getCamera().position.y + playerChunkGeneratorRectangle.height / 2 + 100);

                for (int i = 0; i < 10; i++) {
                    float x = position1.x;
                    float y = (float) (Math.random() * playerChunkGeneratorRectangle.height + position1.y);
                    particles.add(new Snowflake(x, y));
                    float x1 = (float) (Math.random() * playerChunkGeneratorRectangle.width + position2.x);
                    float y1 = position2.y;
                    particles.add(new Snowflake(x1, y1));
                }
            }
        };
        particleGenerationTimer.scheduleTask(task, 0, 0.5f, -1);
        particleGenerationTimer.start();
        shapeRenderer = game.getShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < particles.size; i++) {
            Snowflake snowflake = particles.get(i);

            shapeRenderer.setColor(snowflake.getColor());
            shapeRenderer.rect(snowflake.getRectangle().x,
                    snowflake.getRectangle().y,
                    snowflake.getRectangle().width,
                    snowflake.getRectangle().height);
        }
        shapeRenderer.end();
    }

    private void update(float delta) {
        for (int i = 0; i < particles.size; i++) {
            Snowflake snowflake = particles.get(i);

            if (snowflake.getLifetime() < 0)
                particles.removeValue(particles.get(i), true);

            snowflake.update(delta);
        }
    }

    public Array<Snowflake> getParticles() {
        return particles;
    }
}
