package com.elseff.game.model.food;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public class FriedChicken extends Food {
    public FriedChicken(MyGdxGame game, float x, float y, GameScreen gameScreen) {
        super(game, x, y, gameScreen);
    }

    @Override
    public TextureRegion getTexture() {
        return getGame().getGameResources().findRegion("fried_chicken");
    }
}
