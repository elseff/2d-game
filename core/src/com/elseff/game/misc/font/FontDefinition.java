package com.elseff.game.misc.font;

import java.util.Objects;

public class FontDefinition {
    private final String fontName;
    private final int fontSize;

    public static final FontDefinition ARIAL_15 = new FontDefinition("arial.ttf", 15);
    public static final FontDefinition ARIAL_20 = new FontDefinition("arial.ttf", 20);
    public static final FontDefinition ARIAL_30 = new FontDefinition("arial.ttf", 30);
    public static final FontDefinition ARIAL_50 = new FontDefinition("arial.ttf", 50);

    public FontDefinition(String fontName, int fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FontDefinition that)) return false;
        return getFontSize() == that.getFontSize() &&
                getFontName().equals(that.getFontName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFontName(), getFontSize());
    }

    @Override
    public String toString() {
        return "FontDefinition{" +
                "fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                '}';
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }
}
