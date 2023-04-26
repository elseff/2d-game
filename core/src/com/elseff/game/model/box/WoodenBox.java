package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public class WoodenBox extends Box {
    public WoodenBox(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, gameScreen,x, y);
    }

    @Override
    public TextureRegion getTexture() {
        int randomTextureNumber = (int) (Math.random() * 20+1);
        return getGame().getGameResources().findRegion("box"+randomTextureNumber);
    }
}
