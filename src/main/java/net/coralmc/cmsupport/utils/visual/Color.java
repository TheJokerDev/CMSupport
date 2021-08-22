package net.coralmc.cmsupport.utils.visual;

public class Color {
    private final String colorCode;
    private final int r;
    private final int g;
    private final int b;

    public static Color from(String var0) {
        return new Color(var0);
    }

    public static Color from(int var0, int var1, int var2) {
        java.awt.Color var3 = new java.awt.Color(var0, var1, var2);
        return from(Integer.toHexString(var3.getRGB()).substring(2));
    }

    private Color(String var1) {
        this.colorCode = var1.replace("#", "");
        java.awt.Color var2 = new java.awt.Color(Integer.parseInt(this.colorCode, 16));
        this.r = var2.getRed();
        this.g = var2.getGreen();
        this.b = var2.getBlue();
    }

    public String getTag() {
        return "{#" + this.colorCode + "}";
    }

    public String getColorCode() {
        return this.colorCode;
    }

    public int getRed() {
        return this.r;
    }

    public int getGreen() {
        return this.g;
    }

    public int getBlue() {
        return this.b;
    }

    public String getAppliedTag() {
        return MinecraftColor.getClosest(this).getAppliedTag();
    }

    public String getColorTag() {
        return "{#" + this.colorCode + "}";
    }

    public static int difference(Color var0, Color var1) {
        return Math.abs(var0.r - var1.r) + Math.abs(var0.g - var1.g) + Math.abs(var0.b - var1.b);
    }

    public String toString() {
        return this.getAppliedTag();
    }
}
