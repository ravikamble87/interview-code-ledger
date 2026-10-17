package dev.ravikamble.corejava.flipper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BitFlipperProblemTest {

    static Stream<Arguments> implementations() {
        return Stream.of(
                Arguments.of(Named.of("LibMathMethodSolution",    new LibMathMethodSolution())),
                Arguments.of(Named.of("ComparisonMethodSolution", new ComparisonMethodSolution()))
        );
    }

    // ---------------------------------------------------------------------
    // Contract: every implementation must pass these
    // ---------------------------------------------------------------------

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("S1: flip(1) returns 0")
    void flipsOneToZero(BitFlipperProblem flipper) {
        assertEquals(0, flipper.flip(1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("S2: flip(0) returns 1")
    void flipsZeroToOne(BitFlipperProblem flipper) {
        assertEquals(1, flipper.flip(0));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("S3: flip(flip(x)) returns x")
    void roundTripReturnsOriginal(BitFlipperProblem flipper) {
        assertEquals(0, flipper.flip(flipper.flip(0)));
        assertEquals(1, flipper.flip(flipper.flip(1)));
    }

    // ---------------------------------------------------------------------
    // LibMathMethodSolution: Math.subtractExact(1, input)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("E1: LibMath fails silently on input above 1")
    void libMathReturnsWrongValueAboveOne() {
        BitFlipperProblem flipper = new LibMathMethodSolution();
        assertEquals(-1, flipper.flip(2));
        assertEquals(-99, flipper.flip(100));
    }

    @Test
    @DisplayName("E2: LibMath fails silently on negative input")
    void libMathReturnsWrongValueOnNegative() {
        BitFlipperProblem flipper = new LibMathMethodSolution();
        assertEquals(2, flipper.flip(-1));
    }

    @Test
    @DisplayName("E3: LibMath handles MAX_VALUE but overflows at MIN_VALUE")
    void libMathBoundaryValues() {
        BitFlipperProblem flipper = new LibMathMethodSolution();
        assertEquals(1 - Integer.MAX_VALUE, flipper.flip(Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> flipper.flip(Integer.MIN_VALUE));
    }

    // ---------------------------------------------------------------------
    // ComparisonMethodSolution: Integer.compare(1, input)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("E1: Comparison fails silently on input above 1")
    void comparisonReturnsWrongValueAboveOne() {
        BitFlipperProblem flipper = new ComparisonMethodSolution();
        assertEquals(-1, flipper.flip(2));
        assertEquals(-1, flipper.flip(100));
    }

    @Test
    @DisplayName("E2: Comparison returns 1 for negative input (looks valid, but isn't)")
    void comparisonReturnsMisleadingValueOnNegative() {
        BitFlipperProblem flipper = new ComparisonMethodSolution();
        assertEquals(1, flipper.flip(-1));
    }

    @Test
    @DisplayName("E3: Comparison never throws at boundary values")
    void comparisonBoundaryValues() {
        BitFlipperProblem flipper = new ComparisonMethodSolution();
        assertEquals(-1, flipper.flip(Integer.MAX_VALUE));
        assertEquals(1, flipper.flip(Integer.MIN_VALUE));
    }
}