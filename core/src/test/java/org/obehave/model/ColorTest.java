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
        assertEquals(c.getRed(), 10);
        assertEquals(c.getGreen(), 11);
        assertEquals(c.getBlue(), 12);
        assertEquals(c.getOpacity(), 255);
    }

    @Test
    public void colorWithGray() {
        Color c = new Color(10);
        assertEquals(c.getRed(), 10);
        assertEquals(c.getGreen(), 10);
        assertEquals(c.getBlue(), 10);
        assertEquals(c.getOpacity(), 255);
    }

    @Test
    public void colorWithRGBOpacity() {
        Color c = new Color(0xAF, 0xBF, 0xCF, 0xAB);
        assertEquals(c.getRed(), 0xAF);
        assertEquals(c.getGreen(), 0xBF);
        assertEquals(c.getBlue(), 0xCF);
        assertEquals(c.getOpacity(), 0xAB);
    }

    @Test
    public void valueOfWith6Digits() {
        Color c = Color.valueOf("FF1000");
        assertEquals(c.getRed(), 0xFF);
        assertEquals(c.getGreen(), 0x10);
        assertEquals(c.getBlue(), 0x00);
        assertEquals(c.getOpacity(), 0xFF);
    }

    @Test
    public void valueOfWith8Digits() {
        Color c = Color.valueOf("FF100043");
        assertEquals(c.getRed(), 0xFF);
        assertEquals(c.getGreen(), 0x10);
        assertEquals(c.getBlue(), 0x00);
        assertEquals(c.getOpacity(), 0x43);
    }

    @Test
    public void valueOfWith6DigitsAndHash() {
        Color c = Color.valueOf("#FF1000");
        assertEquals(c.getRed(), 0xFF);
        assertEquals(c.getGreen(), 0x10);
        assertEquals(c.getBlue(), 0x00);
        assertEquals(c.getOpacity(), 0xFF);
    }

    @Test
    public void valueOfWith8DigitsAndHash() {
        Color c = Color.valueOf("#FF100043");
        assertEquals(c.getRed(), 0xFF);
        assertEquals(c.getGreen(), 0x10);
        assertEquals(c.getBlue(), 0x00);
        assertEquals(c.getOpacity(), 0x43);
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
