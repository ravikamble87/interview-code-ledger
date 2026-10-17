package dev.ravikamble.arrays.oddevensorter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Contract tests for {@link OddEvenSorterProblem}.
 *
 * Every test runs against every implementation listed in implementations().
 * To test a new solution, implement the interface and add one line there.
 *
 * A solution documented as valid only for non-negative input is registered
 * with {@link Support#NON_NEGATIVE_ONLY}; tests that need negative numbers
 * are then skipped for it instead of failing.
 */
@DisplayName("#001 — Sort Odds Descending, Evens Ascending")
@ParameterizedClass(name = "{0}")
@MethodSource("implementations")
class OddEvenSorterProblemTest {

    enum Support { ANY_INTEGER, NON_NEGATIVE_ONLY }

    static List<Arguments> implementations() {
        return List.of(
                Arguments.of(Named.of("PartitionSolution", new PartitionSolution()), Support.ANY_INTEGER),
                Arguments.of(Named.of("NegationSolution", new NegationSolution()), Support.NON_NEGATIVE_ONLY)
                // Arguments.of(Named.of("YourNextSolution", new YourNextSolution()), Support.ANY_INTEGER)
        );
    }

    @Parameter(0)
    OddEvenSorterProblem sorter;

    @Parameter(1)
    Support support;

    // ---------------------------------------------------------------------
    // Scenarios: non-negative input, every solution must pass
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Scenarios")
    class Scenarios {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("dev.ravikamble.arrays.oddevensorter.OddEvenSorterProblemTest#nonNegativeCases")
        @DisplayName("Non-negative input")
        void sortsNonNegativeInput(int[] input, int[] expected) {
            assertArrayEquals(expected, sorter.sort(input));
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: negatives and int extremes (skipped for NON_NEGATIVE_ONLY)
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: negatives")
    class NegativeEdgeCases {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("dev.ravikamble.arrays.oddevensorter.OddEvenSorterProblemTest#negativeCases")
        @DisplayName("Input with negatives")
        void sortsInputWithNegatives(int[] input, int[] expected) {
            assumeTrue(support == Support.ANY_INTEGER, "Solution supports non-negative input only");
            assertArrayEquals(expected, sorter.sort(input));
        }

        @Test
        @DisplayName("Matches reference on 1,000 random arrays with negatives")
        void matchesReferenceOnRandomInput() {
            assumeTrue(support == Support.ANY_INTEGER, "Solution supports non-negative input only");
            assertMatchesReference(-1_000, 1_000);
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: contract
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: contract")
    class ContractEdgeCases {

        @Test
        @DisplayName("Does not modify its input")
        void doesNotModifyInput() {
            int[] input = {12, 1, 8, 6, 7, 9, 10, 5, 6};
            int[] snapshot = input.clone();

            sorter.sort(input);

            assertArrayEquals(snapshot, input, "Caller's array was mutated");
        }

        @Test
        @DisplayName("Null input throws NullPointerException")
        void nullInputThrows() {
            assertThrows(NullPointerException.class, () -> sorter.sort(null));
        }

        @Test
        @DisplayName("Matches reference on 1,000 random non-negative arrays")
        void matchesReferenceOnRandomNonNegativeInput() {
            assertMatchesReference(0, 1_000);
        }
    }

    // ---------------------------------------------------------------------
    // Test cases: {input, expected}
    // ---------------------------------------------------------------------

    static Stream<Arguments> nonNegativeCases() {
        return Stream.of(
                Arguments.of(new int[]{12, 1, 8, 6, 7, 9, 10, 5, 6}, new int[]{9, 7, 5, 1, 6, 6, 8, 10, 12}), // Example 1
                Arguments.of(new int[]{3, 11, 1}, new int[]{11, 3, 1}),                                     // only odds
                Arguments.of(new int[]{8, 2, 4}, new int[]{2, 4, 8}),                                       // only evens
                Arguments.of(new int[]{}, new int[]{}),                                                     // empty
                Arguments.of(new int[]{7}, new int[]{7}),                                                   // single odd
                Arguments.of(new int[]{2}, new int[]{2}),                                                   // single even
                Arguments.of(new int[]{0, 1}, new int[]{1, 0}),                                             // zero is even
                Arguments.of(new int[]{5, 2, 5, 2}, new int[]{5, 5, 2, 2}),                                 // duplicates
                Arguments.of(new int[]{9, 7, 2, 4}, new int[]{9, 7, 2, 4})                                  // already sorted
        );
    }

    static Stream<Arguments> negativeCases() {
        return Stream.of(
                Arguments.of(new int[]{-3, 4, 0, -7, 2, 5}, new int[]{5, -3, -7, 0, 2, 4}),                 // Example 2
                Arguments.of(new int[]{-4, 3}, new int[]{3, -4}),                                           // negative even
                Arguments.of(new int[]{-1, -2, -3, -4}, new int[]{-1, -3, -4, -2}),                         // all negative
                Arguments.of(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE},                               // int extremes
                        new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE})
        );
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private void assertMatchesReference(int minInclusive, int maxExclusive) {
        Random random = new Random(42); // fixed seed -> reproducible failures
        for (int run = 0; run < 1_000; run++) {
            int[] input = random.ints(random.nextInt(50), minInclusive, maxExclusive).toArray();
            assertArrayEquals(reference(input), sorter.sort(input.clone()),
                    () -> "Failed for input " + Arrays.toString(input));
        }
    }

    /** Test oracle: deliberately simple, obviously correct, not optimised. */
    private static int[] reference(int[] input) {
        return IntStream.concat(
                Arrays.stream(input).filter(n -> n % 2 != 0).boxed()
                        .sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue),
                Arrays.stream(input).filter(n -> n % 2 == 0).sorted()
        ).toArray();
    }
}
