package com.elseff.game.util;

import com.badlogic.gdx.math.Vector2;

public class MathUtils {
    private static final Vector2 vector = new Vector2();

    public static double distanceBetweenTwoPoints(Vector2 pointPosition1, Vector2 pointPosition2) {
        float x1 = pointPosition1.x;
        float y1 = pointPosition1.y;
        float x2 = pointPosition2.x;
        float y2 = pointPosition2.y;

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow((y2 - y1), 2));
    }

    public static Vector2 middleOfTwoPoints(Vector2 pointPosition1, Vector2 pointPosition2) {
        float x1 = pointPosition1.x;
        float y1 = pointPosition1.y;
        float x2 = pointPosition2.x;
        float y2 = pointPosition2.y;

        vector.set((x1 + x2) / 2, (y1 + y2) / 2);

        return vector;
    }
}
