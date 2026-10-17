package dev.ravikamble.corejava.flipper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contract tests for {@link BitFlipperProblem}.
 *
 * Every test runs against every implementation listed in implementations().
 * To test a new solution, implement the interface and add one line there.
 *
 * The problem leaves behaviour for input outside {0, 1} open (E1-E3:
 * "throw, return garbage, or undefined?"), so those tests only check that a
 * solution either fails fast with a RuntimeException or returns a value,
 * and does so consistently. They never require one specific answer.
 */
@DisplayName("#004 — Bit Flipper Without Operators")
@ParameterizedClass(name = "{0}")
@MethodSource("implementations")
class BitFlipperProblemTest {

    static List<Arguments> implementations() {
        return List.of(
                Arguments.of(Named.of("LibMathMethodSolution", new LibMathMethodSolution())),
                Arguments.of(Named.of("ComparisonMethodSolution", new ComparisonMethodSolution()))
                // Arguments.of(Named.of("YourNextSolution", new YourNextSolution()))
        );
    }

    @Parameter
    BitFlipperProblem flipper;

    // ---------------------------------------------------------------------
    // Scenarios: every solution must produce exactly this
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Scenarios")
    class Scenarios {

        @Test
        @DisplayName("S1: flip(1) returns 0")
        void flipsOneToZero() {
            assertEquals(0, flipper.flip(1));
        }

        @Test
        @DisplayName("S2: flip(0) returns 1")
        void flipsZeroToOne() {
            assertEquals(1, flipper.flip(0));
        }

        @Test
        @DisplayName("S3: flip(flip(x)) returns x")
        void roundTripReturnsOriginal() {
            assertEquals(0, flipper.flip(flipper.flip(0)));
            assertEquals(1, flipper.flip(flipper.flip(1)));
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: input outside {0, 1} (behaviour open, must be consistent)
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: invalid input")
    class InvalidInputEdgeCases {

        @ParameterizedTest(name = "flip({0})")
        @ValueSource(ints = {2, 5, 100})
        @DisplayName("E1: Input above 1 -> throws, or returns a value")
        void inputAboveOne(int input) {
            assertThrowsOrReturnsConsistently(input);
        }

        @ParameterizedTest(name = "flip({0})")
        @ValueSource(ints = {-1, -100})
        @DisplayName("E2: Negative input -> throws, or returns a value")
        void negativeInput(int input) {
            assertThrowsOrReturnsConsistently(input);
        }

        @ParameterizedTest(name = "flip({0})")
        @ValueSource(ints = {Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("E3: Boundary values -> throws, or returns a value")
        void boundaryValues(int input) {
            assertThrowsOrReturnsConsistently(input);
        }
    }

    // ---------------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------------

    /**
     * Passes if the solution fails fast with a RuntimeException every time,
     * or returns the same value every time. An Error (e.g. StackOverflowError)
     * fails the test, and so does an answer that changes between calls.
     */
    private void assertThrowsOrReturnsConsistently(int input) {
        Integer first = flipOrNull(input);
        Integer second = flipOrNull(input);

        assertEquals(first, second, "flip(" + input + ") must behave the same on every call");
    }

    private Integer flipOrNull(int input) {
        try {
            return flipper.flip(input);
        } catch (RuntimeException e) {
            return null;   // fail fast is an accepted choice
        }
    }
}
