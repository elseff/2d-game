package com.elseff.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PopUpMessagesController {
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final Array<PopUpMessage> messages;

    public PopUpMessagesController(MyGdxGame game) {
        messages = new Array<>();
        batch = game.getBatch();
        font = game.getGameResources().updateFontSize(game.getFont(), 20);
    }

    public void render(float delta) {
        update(delta);

        for (int i = 0; i < messages.size; i++) {
            PopUpMessage message = messages.get(i);
            font.setColor(message.getColor());
            font.draw(batch, message.getText(), message.getPosition().x, message.getPosition().y);
        }
    }

    private void update(float delta) {
        for (int i = 0; i < messages.size; i++) {
            PopUpMessage message = messages.get(i);

            checkRemoveMessage(message);
            move(message, delta);
            message.subtractLife(1.5f);
        }
    }

    public void addMessage(PopUpMessage message) {
        messages.add(message);
    }

    private void move(PopUpMessage message, float delta) {
        Vector2 position = message.getPosition();
        message.getPosition().set(position.x, position.y + delta * 50f);
    }

    private void checkRemoveMessage(PopUpMessage message) {
        if (!message.isAlive())
            messages.removeValue(message, true);
    }

    public Array<PopUpMessage> getMessages() {
        return messages;
    }
}
