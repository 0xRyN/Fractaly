package org.fractaly.screens;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.fractaly.utils.Complex;
import org.fractaly.utils.Julia;

public class Fractal extends WritableImage {

    private final int w; // Required
    private final int h; // Required
    private final int maxIter; // Optional
    private final Function<Complex, Complex> juliaFunction; // Optional
    private final BiFunction<Integer, Integer, Color> colorFunction;

    private Fractal(int w, int h, int maxIter, Function<Complex, Complex> juliaFunction,
            BiFunction<Integer, Integer, Color> colorFunction) {
        super(w, h);
        this.w = w;
        this.h = h;
        this.maxIter = maxIter;
        this.juliaFunction = juliaFunction;
        this.colorFunction = colorFunction;
    }

    public static class Builder {

        private final int w; // Required
        private final int h; // Required
        private int maxIter; // Optional
        private Function<Complex, Complex> juliaFunction; // Optional
        private BiFunction<Integer, Integer, Color> colorFunction; // Optional

        /**
         * Initialize a Fractal Builder. You can chain commands like : new
         * Fractal.Builder(100, 100).maxIter(120).julia(f)
         * 
         * At the end of the builder, after you have passed in all arguments, you can
         * call build() to return an object of type Fractal.
         * 
         * @see Fractal
         * @param w REQ int width : width of the Fractal
         * @param h REQ int height : height of the Fractal
         */
        public Builder(int w, int h) {
            this.w = w;
            this.h = h;
            this.maxIter = 50; // Default values
            this.juliaFunction = z -> z.multiply(z).add(Complex.build(-0.7, 0.27015)); // Default values
            this.colorFunction = (i, maxI) -> Color.hsb((255 * i / maxI) % 360, 1, i < maxI ? 1 : 0); // Default values
        }

        /**
         * Setter for the Builder. Sets up the maxIter variable for the julia function.
         * 
         * @param maxIter The maximum number of iterations of the Julia function
         * @return Builder, you can continue piping commands.
         */
        public Builder maxIter(int maxIter) {
            this.maxIter = maxIter;
            return this;
        }

        /**
         * Setter for the Builder. Sets up the julia function variable.
         * 
         * @param f The julia Function<Complex, Complex> to use when computing the
         *          fractals. You can use a lambda, ie : c -> z.multiply(z).add(c)
         * @return Builder, you can continue piping commands.
         */
        public Builder juliaFunction(Function<Complex, Complex> juliaFunction) {
            this.juliaFunction = juliaFunction;
            return this;
        }

        public Builder colorFunction(BiFunction<Integer, Integer, Color> colorFunction) {
            this.colorFunction = colorFunction;
            return this;
        }

        /**
         * After setting up all optional arguments, you can call build() to return the
         * corresponding fractal. It will be all set up, you will just need to display
         * it.
         * 
         * @return Fractal f, the corresponding fractal
         */
        public Fractal build() {
            Fractal res = new Fractal(this.w, this.h, this.maxIter, this.juliaFunction, this.colorFunction);
            Julia.build(res.getPixelWriter(), this.w, this.h, this.maxIter, this.juliaFunction, this.colorFunction);
            return res;
        }

    }

}
