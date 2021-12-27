package org.fractaly.utils;

import java.util.function.BiFunction;

import javafx.scene.paint.Color;

public class FractalColors {

    private FractalColors() {
    }

    public static final BiFunction<Integer, Integer, Color> GRAY_SCALE = ((iter, maxIter) -> {
        int c;
        if (maxIter - iter == 0) {
            c = 0;
        } else {
            c = iter * 255 / maxIter;
        }
        return Color.rgb(c, c, c);
    });

    public static final BiFunction<Integer, Integer, Color> RED_SCALE = ((iter, maxIter) -> {
        int c;
        if (maxIter - iter == 0) {
            c = 0;
        } else {
            c = iter * 255 / maxIter;
        }
        return Color.rgb(c, 0, 0);
    });

    public static final BiFunction<Integer, Integer, Color> GREEN_SCALE = ((iter, maxIter) -> {
        int c;
        if (maxIter - iter == 0) {
            c = 0;
        } else {
            c = iter * 255 / maxIter;
        }
        return Color.rgb(0, c, 0);
    });

    public static final BiFunction<Integer, Integer, Color> BLUE_SCALE = ((iter, maxIter) -> {
        int c;
        if (maxIter - iter == 0) {
            c = 0;
        } else {
            c = iter * 255 / maxIter;
        }
        return Color.rgb(0, 0, c);
    });

    public static final BiFunction<Integer, Integer, Color> RAINBOW = ((iter, maxIter) -> Color
            .hsb((255 * iter / maxIter) % 255, 1, iter < maxIter ? 1 : 0));

}
