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
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getOpacity() {
        return opacity;
    }
}
