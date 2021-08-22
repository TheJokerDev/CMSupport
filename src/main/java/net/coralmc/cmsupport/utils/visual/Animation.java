package net.coralmc.cmsupport.utils.visual;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

public class Animation {
    private static final HashMap<String, List<String>> animationCache = new HashMap();

    public Animation() {
    }

    public static String wave(String var0, Color... var1) {
        return wave(var0, true, 5, 10, var1);
    }

    public static String wave(String string, boolean isBold, int min, int max, Color... colors) {
        Preconditions.checkArgument(colors.length > 1, "Not enough colors provided");
        String var5 = "wave-" + string + "-" + isBold + "-" + min + "-" + max + "-" + Arrays.stream(colors).map(Color::getColorCode).collect(Collectors.joining("-"));
        if (animationCache.containsKey(var5)) {
            return currentFrame(animationCache.get(var5));
        } else {
            List<String> var6 = new ArrayList<>();
            int var7 = 0;
            Color[] var8 = colors;
            int var9 = colors.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Color var11 = var8[var10];
                Color var12 = colors[colors.length == var7 + 1 ? 0 : var7 + 1];
                var6.addAll(Collections.nCopies(min, var11 + (isBold ? "§l" : "") + string));
                List<String> var13 = new ArrayList<>();
                var13.addAll(Collections.nCopies(string.length(), var11.getAppliedTag()));
                var13.addAll(getColorsInbetween(var11, var12, max).stream().map(Color::getAppliedTag).collect(Collectors.toList()));
                var13.addAll(Collections.nCopies(string.length(), var12.getAppliedTag()));

                for(int var14 = 0; var14 <= var13.size() - string.length(); ++var14) {
                    StringBuilder var15 = new StringBuilder();
                    int var16 = 0;
                    char[] var17 = string.toCharArray();

                    for (char var20 : var17) {
                        String var21 = var13.get(var16 + var14);
                        var15.append(var21).append(isBold ? "§l" : "").append(var20);
                        ++var16;
                    }

                    var6.add(var15.toString());
                }

                var6.addAll(Collections.nCopies(min, var12 + (isBold ? "§l" : "") + string));
                ++var7;
            }

            animationCache.put(var5, var6);
            return currentFrame(var6);
        }
    }

    public static String fading(String var0, Color... var1) {
        return fading(var0, true, 10, 20, var1);
    }

    public static String fading(String string, boolean isBold, int min, int max, Color... colors) {
        Preconditions.checkArgument(colors.length > 1, "Not enough colors provided");
        String var5 = "fading-" + string + "-" + isBold + "-" + min + "-" + max + "-" + Arrays.stream(colors).map(Color::getColorCode).collect(Collectors.joining("-"));
        if (animationCache.containsKey(var5)) {
            return currentFrame((List)animationCache.get(var5));
        } else {
            ArrayList var6 = new ArrayList();
            int var7 = 0;
            Color[] var8 = colors;
            int var9 = colors.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Color var11 = var8[var10];
                Color var12 = colors[colors.length == var7 + 1 ? 0 : var7 + 1];
                var6.addAll(Collections.nCopies(min, var11.getAppliedTag() + (isBold ? "§l" : "") + string));
                Iterator var13 = getColorsInbetween(var11, var12, max).iterator();

                while(var13.hasNext()) {
                    Color var14 = (Color)var13.next();
                    var6.add(var14.getAppliedTag() + (isBold ? "§l" : "") + string);
                }

                ++var7;
            }

            animationCache.put(var5, var6);
            return currentFrame(var6);
        }
    }

    private static String currentFrame(List<String> var0) {
        long var1 = System.currentTimeMillis() / 50L;
        int var3 = (int)(var1 % (long)var0.size());
        return (String)var0.get(var3);
    }

    public static List<Color> getColorsInbetween(Color var0, Color var1, int var2) {
        double var3 = (double)(var1.getRed() - var0.getRed()) / (double)var2;
        double var5 = (double)(var1.getGreen() - var0.getGreen()) / (double)var2;
        double var7 = (double)(var1.getBlue() - var0.getBlue()) / (double)var2;
        ArrayList var9 = new ArrayList();

        for(int var10 = 1; var10 <= var2; ++var10) {
            int var11 = (int)Math.round((double)var0.getRed() + var3 * (double)var10);
            int var12 = (int)Math.round((double)var0.getGreen() + var5 * (double)var10);
            int var13 = (int)Math.round((double)var0.getBlue() + var7 * (double)var10);
            Color var14 = Color.from(var11, var12, var13);
            var9.add(var14);
        }

        return var9;
    }
}