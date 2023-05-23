package com.elseff.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.elseff.game.MyGdxGame;

public class MouseController {
    private final MyGdxGame game;
    private final Vector2 position;
    private final Vector2 worldPosition;
    private final Vector3 tempVector3;

    public MouseController(MyGdxGame game) {
        this.game = game;
        position = new Vector2();
        worldPosition = new Vector2();
        tempVector3 = new Vector3();
    }

    public void update(float delta) {
        position.set(Gdx.input.getX(),
                game.getSCREEN_HEIGHT() - Gdx.input.getY());
        Camera camera = game.getGameScreen().getViewport().getCamera();
        tempVector3.set(game.getGameScreen().getPlayer().getPosition().x - game.getSCREEN_WIDTH() / 2f,
                game.getGameScreen().getPlayer().getPosition().y - game.getSCREEN_HEIGHT() / 2f, 0);
        worldPosition.set(
                tempVector3.x + position.x,
                tempVector3.y + position.y
        );
    }

    public float getMouseX() {
        return position.x;
    }

    public float getMouseY() {
        return position.y;
    }

    public float getWorldMouseX() {
        return worldPosition.x;
    }

    public float getWorldMouseY() {
        return worldPosition.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getWorldMousePosition() {
        return worldPosition;
    }
}
