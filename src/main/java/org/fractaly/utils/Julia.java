package org.fractaly.utils;

import java.util.function.UnaryOperator;

public class Julia {
    private Julia() {
    }

    public static Integer compute(int maxIter, Complex c, UnaryOperator<Complex> function) {
        Complex z = c;
        int iter = 0;

        while (z.getMod() < Math.max(z.getMod(), 2) && iter < maxIter) {
            z = function.apply(z);
            iter++;
        }
        return iter;
    }
}
