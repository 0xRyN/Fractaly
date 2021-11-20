package org.fractaly.screens;

import javafx.scene.image.WritableImage;

import org.fractaly.utils.Complex;
import org.fractaly.utils.Julia;

public class Fractal extends WritableImage {
    private Fractal(int w, int h) {
        super(w, h);
    }

    /**
     * Builds and returns a Fractal. A Fractal is a WritableImage which pixels have
     * been computed to display a Julia fractal. Julia parameters : c = -0.7 +
     * 0.27015i
     * 
     * @param w int width : width of the fractal (in pixels)
     * @param h int height : height of the fractal (in pixels)
     * @return Returns a new Fractal (A fractal is a WritableImage)
     */
    public static Fractal buildJulia(int w, int h) {
        Fractal x = new Fractal(w, h);
        try {
            Julia.compute(x.getPixelWriter(), w, h, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    /**
     * Builds and returns a Fractal. A Fractal is a WritableImage which pixels have
     * been computed to display a Julia fractal. This version of the functions lets
     * you chose the c complex.
     * 
     * @param w int width : width of the fractal (in pixels)
     * @param h int height : height of the fractal (in pixels)
     * @param c Complex c : the complex to use in the Julia set
     * @return Returns a new Fractal (A fractal is a WritableImage)
     */
    public static Fractal buildJulia(int w, int h, Complex c) {
        Fractal x = new Fractal(w, h);
        try {
            Julia.compute(x.getPixelWriter(), w, h, 300, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

}
