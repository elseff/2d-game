package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.elseff.game.MyGdxGame;

public class WoodenBox extends Box {
    public WoodenBox(MyGdxGame game, float x, float y) {
        super(game, x, y);
    }

    @Override
    public TextureRegion getTexture() {
        return getGame().getGameResources().findRegion("wooden_box");
    }
}
