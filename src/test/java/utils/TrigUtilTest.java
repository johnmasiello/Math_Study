package utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

class TrigUtilTest {

    @BeforeAll
    static void setUp() {
        // TODO set up params
        // TODO set up reports
    }

    @ParameterizedTest
    @MethodSource("sinParams")
    void testSin(double radians, double expectedVal, double delta, int numIterations) {
        assertIsWithinBounds(expectedVal, TrigUtil.sin(radians, numIterations), delta);
    }

    static List<Arguments> sinParams() {
        return List.of(
                Arguments.of(PI / 2, 1.0, .001, 3),
                Arguments.of(-3 * PI / 2, 1.0, .001, 3),
                Arguments.of(-PI / 2, -1.0, .001, 3),
                Arguments.of(0, 0, .001, 3),
                Arguments.of(PI, 0, .001, 3),
                Arguments.of(PI / 6, .5, .001, 3),
                Arguments.of(7 * PI / 6, -0.5, .001, 3),
                Arguments.of(PI / 4, 1 / Math.sqrt(2), .001, 3)

        );
    }

    @ParameterizedTest
    @MethodSource("sinPerturbations")
    void testSin2(double radians, double expectedVal, double delta, int numIterations) {
        assertIsWithinBounds(expectedVal, TrigUtil.sin(radians, numIterations), delta);
    }

    static List<Arguments> sinPerturbations() {
        return List.of(
                Arguments.of(PI / 4, 1 / Math.sqrt(2), 1e-9, 13),
                Arguments.of(PI / 4 - .01, 0.70000047618079050869393240571147, 1e-9, 13),
                Arguments.of(PI / 4 - .001, 0.70639932096985097068945021521118, 1e-9, 13),
                Arguments.of(PI / 4 + .01, 0.71414237610343957216211001197988, 1e-9, 13),
                Arguments.of(PI / 4 + .001, 0.7078135342965218171278488016463, 1e-9, 13),
                Arguments.of(PI / 6, .5, 1e-6, 8)
                );
    }

    @ParameterizedTest
    @MethodSource("cosParams")
    void testCos(double radians, double expectedVal, double delta, int numIterations) {
        assertIsWithinBounds(expectedVal, TrigUtil.cos(radians, numIterations), delta);
    }

    static List<Arguments> cosParams() {
        return List.of(
                Arguments.of(0, 1.0, .001, 3),
                Arguments.of(-2 * PI, 1.0, .001, 3),
                Arguments.of(PI, -1.0, .001, 3),
                Arguments.of(PI / 2, 0, .001, 3),
                Arguments.of(-PI / 2, 0, .001, 3),
                Arguments.of(PI / 3, .5, .001, 4),
                Arguments.of(4 * PI / 3, -0.5, .001, 4),
                Arguments.of(PI / 4, 1 / Math.sqrt(2), .001, 3)
        );
    }

    void assertIsWithinBounds(double expected, double actual, double delta) {
        System.out.println("Got + " + actual);
        assertFalse(Double.isNaN(actual), "Actual is NaN");
        if (Math.abs(expected - actual) >= delta)
            fail(String.format("Actual: %s is outside of Expected Range: %s +/- %g",
                    actual, expected, delta));
    }
}