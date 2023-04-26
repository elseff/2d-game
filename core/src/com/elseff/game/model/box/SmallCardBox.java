package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.elseff.game.MyGdxGame;
import com.elseff.game.screen.GameScreen;

public class SmallCardBox extends Box {
    public SmallCardBox(MyGdxGame game, GameScreen gameScreen, float x, float y) {
        super(game, gameScreen,x, y);
    }

    @Override
    public TextureRegion getTexture() {
        return getGame().getGameResources().findRegion("small_card_box");
    }
}
