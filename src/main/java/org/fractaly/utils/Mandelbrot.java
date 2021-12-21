package org.fractaly.utils;

public class Mandelbrot {
    private Mandelbrot(){}
    public static Integer compute(int maxIter, Complex c) {
        Complex z = Complex.build(0, 0);
        int iter = 0;
        while (z.getMod() < 2 && iter < maxIter) {
            z = z.multiply(z).add(c);
            iter++;
        }
        return iter;
    }

}
