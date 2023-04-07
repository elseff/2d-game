package com.elseff.game.model.box;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.elseff.game.MyGdxGame;

public class BigWoodenBox extends Box {
    public BigWoodenBox(MyGdxGame game, float x, float y) {
        super(game, x, y);
    }

    @Override
    public TextureRegion getTexture() {
        return getGame().getGameResources().findRegion("big_wooden_box");
    }
}