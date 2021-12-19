package org.fractaly.screens;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.fractaly.model.ComputeFractal;
import org.fractaly.utils.Complex;

public class Fractal extends WritableImage {

    private final int w; // Required
    private final int h; // Required
    private final int maxIter; // Optional
    private final double zoom; // Optional
    private final Function<Complex, Complex> juliaFunction; // Optional
    private final BiFunction<Integer, Integer, Color> colorFunction;
    private final boolean isMandelbrot;

    private Fractal(int w, int h, int maxIter, double zoom, Function<Complex, Complex> juliaFunction,
            BiFunction<Integer, Integer, Color> colorFunction, boolean isMandelbrot) {
        super(w, h);
        this.w = w;
        this.h = h;
        this.maxIter = maxIter;
        this.zoom = zoom;
        this.juliaFunction = juliaFunction;
        this.colorFunction = colorFunction;
        this.isMandelbrot = isMandelbrot;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public double getZoom() {
        return zoom;
    }

    public boolean isMandelbrot() {
        return isMandelbrot;
    }

    public BiFunction<Integer, Integer, Color> getColorFunction() {
        return colorFunction;
    }

    public Function<Complex, Complex> getJuliaFunction() {
        return juliaFunction;
    }

    public static class Builder {

        private final int w; // Required
        private final int h; // Required
        private int maxIter; // Optional
        private double zoom; // Optional
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
            this.maxIter = 100; // Default values
            this.zoom = 1.0;
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
         * Setter for the Builder. Sets up the maxIter variable for the julia function.
         * 
         * @param maxIter The maximum number of iterations of the Julia function
         * @return Builder, you can continue piping commands.
         */
        public Builder zoom(double zoom) {
            this.zoom = zoom;
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
        public Fractal buildJulia() {
            Fractal res = new Fractal(this.w, this.h, this.maxIter, this.zoom, this.juliaFunction, this.colorFunction,
                    false);
            ForkJoinPool pool = new ForkJoinPool();
            ComputeFractal f = new ComputeFractal(0, w * h, res);
            pool.invoke(f);
            return res;
        }

        /**
         * After setting up all optional arguments, you can call build() to return the
         * corresponding fractal. It will be all set up, you will just need to display
         * it.
         * 
         * @return Fractal f, the corresponding fractal
         */
        public Fractal buildMandelbrot() {
            Fractal res = new Fractal(this.w, this.h, this.maxIter, this.zoom, this.juliaFunction, this.colorFunction,
                    true);
            ForkJoinPool pool = new ForkJoinPool();
            ComputeFractal f = new ComputeFractal(0, w * h, res);
            pool.invoke(f);
            return res;
        }

    }

}
