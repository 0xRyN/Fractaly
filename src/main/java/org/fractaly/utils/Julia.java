package org.fractaly.utils;

import java.util.function.Function;

public class Julia {

    public static Integer compute(int maxIter, Complex c, Function<Complex, Complex> juliaFunction) {
        Complex z = c;
        int iter = 0;
        while (z.getMod() < 2 && iter < maxIter) {
            z = juliaFunction.apply(z);
            iter++;
        }
        return iter;
    }
}
