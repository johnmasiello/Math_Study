package utils;

import static java.lang.Math.*;

public class TrigUtil {
    /*
     For many inputs, cosine calculation does not converge to actual value,
     whereby increasing number of iterations does not improve accuracy.

     It may be advisable for cosine to be calculated using sine, instead.
     The input to sine can be translated from the input to cosine easily using phase shift.
     Thus, cosine is obtained via pass through to the calculation of sine using the equivalent angle for input.
    */
    private static final int COS_NUMBER_OF_ITERATIONS = 10;

    /*
     On the other hand, it seems that sine does improve accuracy with additional iterations,
     And there may exist a linear relationship between expected precision and number of iterations.
     Convergence to true value does not seem to be quadratic, however.

     There will be an upper limit on number of iterations, and therefore precision of result, due to limitations
     of the calculation/approximation method.

     Specifically, the initial term as calculated will be x/2^n for n
     iterations. As n parameter increases x/2^n term will become too small for the finite calculation environment.
     This fortunately, is alleviated by the fact, IEEE 754 gives the mantissa as 52 bits. This gives us a lot to
     work with, perhaps putting to rest for most the need for dedicated precision types, i.e., BigDecimal.
    */
    private static final int SIN_NUMBER_OF_ITERATIONS = 22;

    static double cosPassThrough(double val, int iterations) {
        return sin(PI / 2 - val);
    }

    static double cos(double val, int iterations) {
        // transform input to domain [0, pi]
        double t = floorModulo(val, 2 * PI);
        final double t2 = t > PI ? 2 * PI - t : t;

        // Restrict domain further to [0, pi/2]...
        // helping to improve calculation convergence to actual value.
        final double x = PI / 2 - abs(PI / 2 - t2);
        final double sign = signum(PI / 2 - t2);

        // Approximate first term using a 'many' half-angle of the input angle/value, restricted to [-pi/2, pi/2].
        // The first term is meant to approximate sin x
        double sin = x / (1 << iterations);

        // The second term is meant to approximate cos (2x)
        double cos = 1 - 2 * sin * sin;
        // remaining n - 1 terms to compute the final term
        for (int i = 1; i < iterations; i++) {
            cos = 2 * cos * cos - 1;
        }
        return sign * cos;
    }

    static double sin(double val, int iterations) {
        // transform input to domain [-pi/2, pi/2]
        double t = floorModulo(PI / 2 - val, 2 * PI);
        final double x = t > PI ? -3 * PI / 2 + t : PI / 2 - t;
        final double sign = signum(x);

        // Approximate first term using a 'many' half-angle of the input angle/value, restricted to [-pi/2, pi/2].
        double sin = x / (1 << iterations);
        // The first term is meant to approximate sin ^ 2 x
        double sin2 = sin * sin;
        // remaining n terms to compute the final term
        for (int i = 0; i < iterations; i++) {
            // Double angle formula applied here is used to double to get to x coterminal angle of input
            // Incidentally, this happens to be an instance of the Logistic Map
            sin2 = 4.0 * sin2 * (1 - sin2);
        }
        return sign * sqrt(sin2);
    }

    public static double cosPassThrough(double val) {
        return cosPassThrough(val, SIN_NUMBER_OF_ITERATIONS);
    }

    public static double cos(double val) {
        return cos(val, COS_NUMBER_OF_ITERATIONS);
    }

    public static double cosDeg(double val) {
        return cos(val / 360.0 * PI);
    }

    public static double sin(double val) {
        return sin(val, SIN_NUMBER_OF_ITERATIONS);
    }

    public static double sinDeg(double val) {
        return sin(val / 360.0 * PI);
    }

    // Given divisor is positive, then the output will always be both nonnegative and < divisor.
    public static double floorModulo(double div, double divisor) {
        double remainder = div % divisor;
        return remainder < 0 ? remainder + divisor : remainder;
    }
}
