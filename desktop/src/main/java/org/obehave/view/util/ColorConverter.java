package org.obehave.view.util;

/**
 * @author Markus Möslinger
 */
public class ColorConverter {
    public static javafx.scene.paint.Color convertToJavaFX(org.obehave.model.Color c) {
        if (c == null) {
            return javafx.scene.paint.Color.BLACK;
        }

        return javafx.scene.paint.Color.color(c.getRed() / 255.0,
                c.getGreen() / 255.0,
                c.getBlue() / 255.0,
                c.getOpacity() / 255.0);
    }

    public static org.obehave.model.Color convertToObehave(javafx.scene.paint.Color c) {
        if (c == null) {
            return new org.obehave.model.Color(0);
        }

        return new org.obehave.model.Color((int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255),
                (int) (c.getOpacity()) * 255);
    }
}
