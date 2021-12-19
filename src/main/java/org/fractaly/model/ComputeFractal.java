package org.fractaly.model;

import java.util.concurrent.RecursiveAction;

import org.fractaly.screens.Fractal;
import org.fractaly.utils.Complex;
import org.fractaly.utils.Julia;

public class ComputeFractal extends RecursiveAction {

    // If image is less than 100x100 = 10000, compute it directly
    private final int threshold = 10000;
    private final int start;
    private final int size;
    private final int w;
    private final int h;
    private final double zoom;
    private Fractal fractal;
    public static int threads = 0;

    public ComputeFractal(int start, int size, Fractal fractal) {
        threads++;
        this.w = fractal.getW();
        this.h = fractal.getH();
        this.zoom = fractal.getZoom();
        this.start = start;
        this.size = size;
        this.fractal = fractal;
    }

    protected void computeDirectly() {
        for (int k = start; k < size; k++) {
            /*
             * HOW IT WORKS
             * Size = width * height.
             * On an array of 10x10, Fractal.width = 10.
             * If for example k = 55, 55/10 = 5, i = 5
             * If for example k = 55, 55 - (i * Fractal.width) = 55 - 50 = 5
             * We deduce that, if k = 55, we are evaluating i = 5, j = 5
             */
            int i = k / w;
            int j = k - (i * w);
            double zi = (-1 + i * (2.0 / this.w)) / zoom;
            double zj = (-1 + j * (2.0 / this.h)) / zoom;
            Complex z = Complex.build(zi, zj);
            int iter = Julia.compute(fractal.getMaxIter(), z, fractal.getJuliaFunction());
            fractal.getPixelWriter().setColor(i, j, fractal.getColorFunction().apply(iter, this.fractal.getMaxIter()));
        }
    }

    // Java ForkJoinPool example :
    // https://docs.oracle.com/javase/tutorial/essential/concurrency/examples/ForkBlur.java
    @Override
    protected void compute() {
        System.out.println("Thread no : " + threads + " - Start : " + start + ", End : " + (start + size));
        if (size >= threshold) {
            int split = size / 2;
            ComputeFractal left = new ComputeFractal(start, split, fractal);
            ComputeFractal right = new ComputeFractal(start + split, size - split, fractal);
            invokeAll(left, right);
        }
        computeDirectly();
    }

}
