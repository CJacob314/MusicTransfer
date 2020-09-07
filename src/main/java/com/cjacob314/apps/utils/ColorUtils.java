package com.cjacob314.apps.utils;

import javafx.scene.paint.Color;

public class ColorUtils {

    private static String formatForHexConv(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public static String colorToHexString(Color value) {
        return "#" + (formatForHexConv(value.getRed()) + formatForHexConv(value.getGreen()) + formatForHexConv(value.getBlue()) + formatForHexConv(value.getOpacity()));
    }

    public static String darkenColorStr(String origHex, double factor){
        Color orig = Color.valueOf(origHex);
        return colorToHexString(orig.deriveColor(0d, 1d, factor, 1d));
    }
}
