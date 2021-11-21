package org.fractaly.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Julia {

    private final PixelWriter pWriter;
    private final int w;
    private final int h;
    private final int maxIter;
    private final Function<Complex, Complex> juliaFunction;
    private final BiFunction<Integer, Integer, Color> colorFunction;

    private Julia(PixelWriter pWriter, int w, int h, int maxIter, Function<Complex, Complex> juliaFunction,
            BiFunction<Integer, Integer, Color> colorFunction) {
        this.pWriter = pWriter;
        this.w = w;
        this.h = h;
        this.maxIter = maxIter;
        this.juliaFunction = juliaFunction;
        this.colorFunction = colorFunction;
    }

    private void iterate(int i, int j, Complex z) {
        int iter = 0;
        while (z.getMod() < 2 && iter < this.maxIter) {
            z = juliaFunction.apply(z);
            iter++;
        }
        this.pWriter.setColor(i, j, colorFunction.apply(iter, this.maxIter));
    }

    private void compute() {
        for (int i = 0; i < this.w; i++) {
            for (int j = 0; j < this.h; j++) {
                Complex z = Complex.build(-1 + i * (2.0 / this.w), -1 + j * (2.0 / this.h));
                iterate(i, j, z);
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
     * @param f       The Julia Function<Complex, Complex> to apply
     */
    public static void build(PixelWriter pWriter, int w, int h, int maxIter, Function<Complex, Complex> juliaFunction,
            BiFunction<Integer, Integer, Color> colorFunction) {
        Julia func = new Julia(pWriter, w, h, maxIter, juliaFunction, colorFunction);
        func.compute();
    }
}
