package org.fractaly.utils;

import java.util.Objects;

// Immutable.

public class Complex {
    private final double re;
    private final double im;
    private final double mod;
    private final double arg; // In radians

    private static double mod(double re, double im) {
        return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
    }

    private static double arg(double re, double im, double mod) {
        if (re > 0 || im != 0) {
            double frac = ((im) / (mod + re));
            return 2 * Math.atan(frac);
        } else if (re < 0) {
            return Math.PI;
        } else
            return 0;
    }

    /**
     * Builds and returns a complex number z
     * 
     * @param re Real part Re(z)
     * @param im Imaginary part Im(z)
     * @return The complex number z
     */
    private Complex(double re, double im, double mod, double arg) {
        this.re = re;
        this.im = im;
        this.mod = mod;
        this.arg = arg;
    }

    /**
     * Statically builds and returns a new complex number. Equivalent to the
     * constructor.
     * 
     * @param re Real part Re(z)
     * @param im Imaginary part Im(z)
     * @return The complex number z
     */
    public static Complex build(double re, double im) {
        double mod = mod(re, im);
        double arg = arg(re, im, mod);
        return new Complex(re, im, mod, arg);
    }

    /**
     * Tests if two complex numbers are equal.
     * 
     * @param a First complex number
     * @param b Second complex number
     * @return True if both numbers are equal. False if they aren't.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Complex b) {
            Complex a = this;
            return (a.getRe() == b.getRe() && a.getIm() == b.getIm());
        } else
            return false;
    }

    /**
     * Returns a hash code value for the object. Two equal objects will return the
     * same hashcode.
     * 
     * @return int hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.re, this.im);
    }

    /**
     * Computes the conjugate of the number. The conjugate of a complex number is
     * itself while inverting the imaginary part.
     * 
     * conjugate(this) = Complex(this.re, -this.im)
     * 
     * @return The conjugate of this.
     */
    public Complex conjugate() {
        Complex a = this;
        return Complex.build(a.getRe(), -a.getIm());
    }

    /**
     * Sums two complex numbers, this and b, and returns a new complex number equal
     * to the sum.
     * 
     * @param b The complex to add.
     * @return this + b. A new complex which is the sum of both.
     */
    public Complex add(Complex b) {
        Complex a = this;
        return Complex.build(a.getRe() + b.getRe(), a.getIm() + b.getIm());
    }

    /**
     * Substract two complex numbers, this and b, and returns a new complex number
     * equal to the substraction.
     * 
     * @param b The complex to substract.
     * @return this - b. A new complex which is the sub of both.
     */
    public Complex sub(Complex b) {
        Complex a = this;
        return Complex.build(a.getRe() - b.getRe(), a.getIm() - b.getIm());
    }

    /**
     * Multiplies two complex numbers, this and b, and returns a new complex number
     * equal to the multiplication.
     * 
     * @param b The complex to multiply.
     * @return this * b. A new complex which is the sub of both.
     */
    public Complex multiply(Complex b) {
        Complex a = this;
        return Complex.build(a.getRe() * b.getRe() - a.getIm() * b.getIm(),
                a.getRe() * b.getIm() + a.getIm() * b.getRe());
    }

    /**
     * Builds and returns a complex number from polar coordinates
     * 
     * @param mod double Modulus : Modulus of the complex number
     * @param arg double Argument : Argument of the complex number (in radians)
     * @return The corresponding complex number
     */
    public static Complex fromPolar(double mod, double arg) {
        double r = mod * Math.cos(arg);
        double i = mod * Math.sin(arg);
        return Complex.build(r, i);
    }

    /**
     * Returns the imaginary part of the complex number this.
     * 
     * @return A double containing the imaginary part of this
     */
    public double getIm() {
        return im;
    }

    /**
     * Returns the real part of the complex number this.
     * 
     * @return A double containing Re(this)
     */
    public double getRe() {
        return re;
    }

    /**
     * Returns the angle Arg(this) in radians of this.
     * 
     * @return A double containing Arg(this) in radians
     */
    public double getArg() {
        return arg;
    }

    /**
     * Returns the length / modulo Mod(this) of this.
     * 
     * @return A double containing Mod(this)
     */
    public double getMod() {
        return mod;
    }

    @Override
    public String toString() {
        return "Re : " + this.re + ", Im: " + this.im;
    }

}
