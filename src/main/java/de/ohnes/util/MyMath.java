package de.ohnes.util;

public class MyMath {

    /**
     * rounds @n up to the next integer multiple of @target.
     * assuming that @n and @target >= 0.
     */
    public static double roundUp(double n, double target) {
        return target * Math.ceil(n / target);
    }
    
}
