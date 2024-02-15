package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public class RandomTextureBox extends Box {
    private final TextureRegion texture;

    public RandomTextureBox(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, gameScreen,x, y);
        int randomTextureNumber = (int) (Math.random() * 20+1);
        texture = getGame().getGameResources().findRegion("box"+randomTextureNumber);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }
}
