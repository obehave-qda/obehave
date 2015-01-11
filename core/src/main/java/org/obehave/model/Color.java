package org.obehave.model;

/**
 * @author Markus Möslinger
 */
public class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final double opacity;

    public Color(int color) {
        this(color, color, color, 1);
    }

    public Color(int red, int green, int blue) {
        this(red, green, blue, 1);
    }

    public Color(int red, int green, int blue, double opacity) {
        if (opacity < 0 || opacity > 1) {
            throw new IllegalArgumentException("Opacity has to be between 0 and 1");
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public double getOpacity() {
        return opacity;
    }
}
