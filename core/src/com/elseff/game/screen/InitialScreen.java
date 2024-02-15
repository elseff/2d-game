package com.elseff.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.elseff.game.MyGdxGame;

public class InitialScreen implements Screen {
    private final MyGdxGame game;
    private final Stage stage;
    private final Skin skin;

    public InitialScreen(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skins/default/skin/uiskin.json"));
//        Label.LabelStyle style = new Label.LabelStyle(game.getGameResources().getFontFromDef(FontDefinition.ARIAL_50), Color.CHARTREUSE);
//        Label label = new Label("NIGGA", style);
//        label.setPosition(100, 100);
//        stage.addActor(label);
        TextButton button = new TextButton("Click me!", skin);
        button.setTouchable(Touchable.enabled);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log("INFO", String.format("Button %s clicked", actor.getName()));
                game.setScreen(game.getGameScreen());
            }
        });
        button.setBounds(200, 200, 105, 50);
        button.setName("Button 'Click me!'");
        stage.addActor(button);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        game.update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
