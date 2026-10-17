package dev.ravikamble.arrays.longestconsecutive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Contract tests for {@link LongestConsecutiveSequenceProblem}.
 *
 * Every test runs against every implementation listed in implementations().
 * To test a new solution, implement the interface and add one line there.
 */
@DisplayName("#002 — Longest Consecutive Sequence")
@ParameterizedClass(name = "{0}")
@MethodSource("implementations")
class LongestConsecutiveSequenceProblemTest {

    static List<Arguments> implementations() {
        return List.of(
                Arguments.of(Named.of("SortingSolution", new SortingSolution())),
                Arguments.of(Named.of("HashSetSolution", new HashSetSolution()))
                // Arguments.of(Named.of("YourNextSolution", new YourNextSolution()))
        );
    }

    @Parameter
    LongestConsecutiveSequenceProblem solver;

    // ---------------------------------------------------------------------
    // Scenarios: the examples from the problem statement
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Scenarios")
    class Scenarios {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("dev.ravikamble.arrays.longestconsecutive.LongestConsecutiveSequenceProblemTest#examples")
        @DisplayName("Examples")
        void examples(int[] input, int expected) {
            assertEquals(expected, solver.longestConsecutive(input));
        }

        @Test
        @Timeout(5)
        @DisplayName("1M shuffled values forming one run")
        void largeInput() {
            int[] input = IntStream.range(0, 1_000_000).toArray();
            shuffle(input, new Random(42));

            assertEquals(1_000_000, solver.longestConsecutive(input));
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases from the problem statement
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("dev.ravikamble.arrays.longestconsecutive.LongestConsecutiveSequenceProblemTest#edgeCases")
        @DisplayName("Edge case inputs")
        void edgeCases(int[] input, int expected) {
            assertEquals(expected, solver.longestConsecutive(input));
        }

        @Test
        @DisplayName("Matches reference on 1,000 random arrays")
        void matchesReferenceOnRandomInput() {
            Random random = new Random(42); // fixed seed -> reproducible failures
            for (int run = 0; run < 1_000; run++) {
                int[] input = random.ints(random.nextInt(50), -50, 50).toArray();
                assertEquals(reference(input), solver.longestConsecutive(input.clone()),
                        () -> "Failed for input " + Arrays.toString(input));
            }
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
            int[] input = {100, 4, 200, 1, 3, 2};
            int[] snapshot = input.clone();

            solver.longestConsecutive(input);

            assertArrayEquals(snapshot, input, "Caller's array was mutated");
        }

        @Test
        @DisplayName("Null input throws NullPointerException")
        void nullInputThrows() {
            assertThrows(NullPointerException.class, () -> solver.longestConsecutive(null));
        }
    }

    // ---------------------------------------------------------------------
    // Test cases: {input, expected}
    // ---------------------------------------------------------------------

    static Stream<Arguments> examples() {
        return Stream.of(
                Arguments.of(new int[]{100, 4, 200, 1, 3, 2}, 4),               // Example 1
                Arguments.of(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}, 9),       // Example 2
                Arguments.of(new int[]{10, 5, 12, 3, 11, 4, 13}, 4),            // Example 3
                Arguments.of(new int[]{-1, -2, 0, 1, 9}, 4)                     // Example 4
        );
    }

    static Stream<Arguments> edgeCases() {
        return Stream.of(
                Arguments.of(new int[]{}, 0),                                   // empty
                Arguments.of(new int[]{7}, 1),                                  // single element
                Arguments.of(new int[]{5, 5, 5}, 1),                            // all the same
                Arguments.of(new int[]{1, 2, 0, 1}, 3),                         // duplicate inside a run
                Arguments.of(new int[]{10, 30, 20}, 1),                         // nothing consecutive
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 5),                      // already sorted
                Arguments.of(new int[]{-3, -1, -2}, 3),                         // all negative
                Arguments.of(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, 1) // overflow must not join
        );
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    /** Test oracle: deliberately simple, obviously correct, not optimised. */
    private static int reference(int[] input) {
        TreeSet<Long> values = new TreeSet<>();
        for (int n : input) {
            values.add((long) n);
        }
        int best = 0;
        int run = 0;
        Long previous = null;
        for (long n : values) {
            run = (previous != null && n == previous + 1) ? run + 1 : 1;
            best = Math.max(best, run);
            previous = n;
        }
        return best;
    }

    private static void shuffle(int[] values, Random random) {
        for (int i = values.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
    }
}
