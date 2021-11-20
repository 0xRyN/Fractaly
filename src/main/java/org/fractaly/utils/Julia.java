package org.fractaly.utils;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Julia {

    private Julia() {
    }

    /**
     * Computes Julia on the given PixelWriter. Julia parameters : c = -0.7 +
     * 0.27015i
     * 
     * @param p       PixelWriter p : the PixelWriter on which Julia is computed
     * @param w       int width : the width of the WritableImage
     * @param h       int height : the height of the WritableImage
     * @param maxIter int maxIter : the maximum number of Julia iterations for each
     *                pixel
     */
    public static void compute(PixelWriter p, int w, int h, int maxIter) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Complex z = Complex.build(-1 + i * (2.0 / w), -1 + j * (2.0 / h));
                Complex c = Complex.build(-0.7, 0.27015);

                int iter = 0;
                while (z.getMod() < 2 && iter < maxIter) {
                    z = z.multiply(z).add(c);
                    iter++;
                }
                p.setColor(i, j, Color.hsb((255 * iter / maxIter) % 255, 1, iter < maxIter ? 1 : 0));
            }
        }
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
     * @param c       Complex c : the complex c to use as a Julia parameter
     */
    public static void compute(PixelWriter p, int w, int h, int maxIter, Complex c) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Complex z = Complex.build(-1 + i * (2.0 / w), -1 + j * (2.0 / h));
                int iter = 0;
                while (z.getMod() < 2 && iter < maxIter) {
                    z = z.multiply(z).add(c);
                    iter++;
                }
                p.setColor(i, j, Color.hsb((255 * iter / maxIter) % 255, 1, iter < maxIter ? 1 : 0));
            }
        }
    }
}
