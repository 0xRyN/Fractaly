package org.fractaly.utils;

import java.util.function.Function;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Julia {

    private Julia() {
    }

    private static void iterate(PixelWriter p, Complex z, Function<Complex, Complex> f, int i, int j, int maxIter) {
        int iter = 0;
        while (z.getMod() < 2 && iter < maxIter) {
            z = f.apply(z);
            iter++;
        }
        p.setColor(i, j, Color.hsb(((255 * iter / maxIter) % 360), 1, iter < maxIter ? 1 : 0));
    }

    /**
     * Computes Julia on the given PixelWriter. This version lets you chose the
     * Complex c as a Julia parameter.
     * 
     * @param p       PixelWriter p : the PixelWriter on which Julia is computed
     * @param w       int width : the width of the WritableImage
     * @param h       int height : the height of the WritableImage
     * @param maxIter int maxIter : the maximum number of Julia iterations for each
     *                pixel
     * @param f       The Julia Function<Complex, Complex> to apply
     */
    public static void compute(PixelWriter p, int w, int h, int maxIter, Function<Complex, Complex> f) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Complex z = Complex.build(-1 + i * (2.0 / w), -1 + j * (2.0 / h));
                iterate(p, z, f, i, j, maxIter);
            }
        }
    }
}
