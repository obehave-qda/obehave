package org.obehave.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class ColorTest {
    @Test
    public void colorWithRGB() {
        Color c = new Color(10, 11, 12);
        assertEquals(10, c.getRed());
        assertEquals(11, c.getGreen());
        assertEquals(12, c.getBlue());
        assertEquals(255, c.getOpacity());
    }

    @Test
    public void colorWithGray() {
        Color c = new Color(10);
        assertEquals(10, c.getRed());
        assertEquals(10, c.getGreen());
        assertEquals(10, c.getBlue());
        assertEquals(255, c.getOpacity());
    }

    @Test
    public void colorWithRGBOpacity() {
        Color c = new Color(0xAF, 0xBF, 0xCF, 0xAB);
        assertEquals(0xAF, c.getRed());
        assertEquals(0xBF, c.getGreen());
        assertEquals(0xCF, c.getBlue());
        assertEquals(0xAB, c.getOpacity());
    }

    @Test
    public void valueOfWith6Digits() {
        Color c = Color.valueOf("FF1000");
        assertEquals(0xFF, c.getRed());
        assertEquals(0x10, c.getGreen());
        assertEquals(0x00, c.getBlue());
        assertEquals(0xFF, c.getOpacity());
    }

    @Test
    public void valueOfWith8Digits() {
        Color c = Color.valueOf("FF100043");
        assertEquals(0xFF, c.getRed());
        assertEquals(0x10, c.getGreen());
        assertEquals(0x00, c.getBlue());
        assertEquals(0x43, c.getOpacity());
    }

    @Test
    public void valueOfWith6DigitsAndHash() {
        Color c = Color.valueOf("#FF1000");
        assertEquals(0xFF, c.getRed());
        assertEquals(0x10, c.getGreen());
        assertEquals(0x00, c.getBlue());
        assertEquals(0xFF, c.getOpacity());
    }

    @Test
    public void valueOfWith8DigitsAndHash() {
        Color c = Color.valueOf("#FF100043");
        assertEquals(0xFF, c.getRed());
        assertEquals(0x10, c.getGreen());
        assertEquals(0x00, c.getBlue());
        assertEquals(0x43, c.getOpacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noOpacityBiggerThan256() {
        new Color(0xFF, 0xFF, 0xFF, 256);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noOpacitySmallerThan0() {
        new Color(0xFF, 0xFF, 0xFF, -1);
    }
}
