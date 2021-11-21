package org.fractaly.screens;

import javafx.scene.image.WritableImage;

import java.util.function.Function;

import org.fractaly.utils.Complex;
import org.fractaly.utils.Julia;

public class Fractal extends WritableImage {

    private final int w; // Required
    private final int h; // Required
    private final int maxIter; // Optional
    private final Function<Complex, Complex> f; // Optional

    private Fractal(int w, int h, int maxIter, Function<Complex, Complex> f) {
        super(w, h);
        this.w = w;
        this.h = h;
        this.maxIter = maxIter;
        this.f = f;
    }

    public static class Builder {

        private final int w; // Required
        private final int h; // Required
        private int maxIter; // Optional
        private Function<Complex, Complex> f; // Optional

        /**
         * 
         * @param w REQ int width :
         * @param h
         */
        public Builder(int w, int h) {
            this.w = w;
            this.h = h;
            this.maxIter = 300;
            this.f = z -> z.multiply(z).add(Complex.build(-0.7, 0.27015));
        }

        public Builder maxIter(int maxIter) {
            this.maxIter = maxIter;
            return this;
        }

        public Builder function(Function<Complex, Complex> f) {
            this.f = f;
            return this;
        }

        public Fractal build() {
            Fractal res = new Fractal(this.w, this.h, this.maxIter, this.f);
            Julia.compute(res.getPixelWriter(), this.w, this.h, this.maxIter, this.f);
            return res;
        }

    }

}
