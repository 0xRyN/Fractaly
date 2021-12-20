package org.fractaly.model;

import java.util.concurrent.RecursiveAction;

import org.fractaly.utils.Complex;
import org.fractaly.utils.Julia;
import org.fractaly.utils.Mandelbrot;
import org.fractaly.view.Fractal;

public class ComputeFractal extends RecursiveAction {

    // If image is less or equal to 100x100 = 10000, compute it directly
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
        for (int k = start; k < start + size; k++) {
            /*
             * HOW IT WORKS
             * Size = width * height.
             * On an array of 10x10, Fractal.width = 10.
             * If for example k = 55, 55/10 = 5, i = 5
             * If for example k = 55, 55 - (i * Fractal.width) = 55 - 50 = 5
             * We deduce that, if k = 55, we are evaluating i = 5, j = 5
             */
            int y = k / w; // Height - j index
            int x = k - (y * w); // Width - i index

            double zx = (-1 + x * (2.0 / this.w)) / zoom;
            double zy = (-1 + y * (2.0 / this.h)) / zoom;
            Complex z = Complex.build(zx, zy);

            int iter = 0;

            if (fractal.isMandelbrot()) {
                iter = Mandelbrot.compute(fractal.getMaxIter(), z);
            } else {
                iter = Julia.compute(fractal.getMaxIter(), z, fractal.getJuliaFunction());
            }
            try {
                fractal.getPixelWriter().setColor(x, y,
                        fractal.getColorFunction().apply(iter, this.fractal.getMaxIter()));
            } catch (Exception e) {
                System.out.println("Error when writing to WritableImage !");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    // Java ForkJoinPool example :
    // https://docs.oracle.com/javase/tutorial/essential/concurrency/examples/ForkBlur.java
    @Override
    protected void compute() {
        // System.out.println("Thread no : " + threads + " - Start : " + start + ", End
        // : " + (start + size));
        if (size <= threshold) {
            computeDirectly();
            return;

        }
        int split = size / 2;
        ComputeFractal left = new ComputeFractal(start, split, fractal);
        ComputeFractal right = new ComputeFractal(start + split, size - split, fractal);
        invokeAll(left, right);

    }

}
