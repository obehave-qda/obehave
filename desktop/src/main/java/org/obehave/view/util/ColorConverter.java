package org.obehave.view.util;

/**
 * Converts instances of {@link org.obehave.model.Color} to {@link javafx.scene.paint.Color} and vice versa
 */
public class ColorConverter {
    /**
     * Converts an obehave color to a JavaFX one
     * @param color an obehave color
     * @return a JavaFX color
     */
    public static javafx.scene.paint.Color convertToJavaFX(org.obehave.model.Color color) {
        if (color == null) {
            return javafx.scene.paint.Color.BLACK;
        }

        return javafx.scene.paint.Color.color(color.getRed() / 255.0,
                color.getGreen() / 255.0,
                color.getBlue() / 255.0,
                color.getOpacity() / 255.0);
    }

    /**
     * Converts a JavaFX color to an obehave one
     * @param color a JavaFX color
     * @return an obehave color
     */
    public static org.obehave.model.Color convertToObehave(javafx.scene.paint.Color color) {
        if (color == null) {
            return new org.obehave.model.Color(0);
        }

        return new org.obehave.model.Color((int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                (int) (color.getOpacity()) * 255);
    }
}
