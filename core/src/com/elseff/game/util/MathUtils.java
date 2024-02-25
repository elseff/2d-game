package com.elseff.game.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MathUtils {
    private static final Vector2 tmpVector1 = new Vector2();
    private static final Vector2 tmpVector2 = new Vector2();

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

        tmpVector1.set((x1 + x2) / 2, (y1 + y2) / 2);

        return tmpVector1;
    }

    public static boolean isCircleOverlapsWithRectangle(Circle circle, Rectangle rectangle) {
        Vector2 circleCenter = tmpVector2.set(circle.x, circle.y);
        Vector2 rectangleCenter = tmpVector1.set(rectangle.x, rectangle.y);

        float rectangleCenterX = rectangleCenter.x;
        float rectangleCenterY = rectangleCenter.y;
        double rectangleCircumscribedCircleRadius = Math.sqrt(Math.pow(rectangleCenterX, 2) + Math.pow(rectangleCenterY, 2)) / 2;

        double distanceBetweenCenters = distanceBetweenTwoPoints(circleCenter, rectangle.getCenter(tmpVector1));

        if (distanceBetweenCenters > (rectangleCircumscribedCircleRadius + circle.radius))
            return false;

        float hH = rectangle.height / 2f;
        float hW = rectangle.width / 2f;

        return (distanceBetweenCenters < hH + circle.radius) ||
                (distanceBetweenCenters > hH + circle.radius && distanceBetweenCenters < hW + circle.radius);
    }
}
