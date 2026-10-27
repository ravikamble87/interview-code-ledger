package dev.ravikamble.arrays.twosum;

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
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Contract tests for {@link TwoSumProblem}.
 *
 * Every test runs against every implementation listed in implementations().
 * To test a new solution, implement the interface and add one line there.
 *
 * The indices may be returned in any order, so results are sorted before they
 * are compared with the expected indices.
 */
@DisplayName("#005 — Two Sum")
@ParameterizedClass(name = "{0}")
@MethodSource("implementations")
class TwoSumProblemTest {

    static List<Arguments> implementations() {
        return List.of(
                Arguments.of(Named.of("TwoLoopSolution", new TwoLoopSolution())),
                Arguments.of(Named.of("HashMapSolution", new HashMapSolution()))
                // Arguments.of(Named.of("YourNextSolution", new YourNextSolution()))
        );
    }

    @Parameter
    TwoSumProblem twoSum;

    // ---------------------------------------------------------------------
    // Scenarios: the examples from the problem statement
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Scenarios")
    class Scenarios {

        @ParameterizedTest(name = "{0}, target {1} -> {2}")
        @MethodSource("dev.ravikamble.arrays.twosum.TwoSumProblemTest#exampleCases")
        @DisplayName("Examples")
        void findsThePair(int[] nums, int target, int[] expected) {
            assertIndices(expected, twoSum.findIndices(nums, target));
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: values and positions
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: values")
    class ValueEdgeCases {

        @ParameterizedTest(name = "{0}, target {1} -> {2}")
        @MethodSource("dev.ravikamble.arrays.twosum.TwoSumProblemTest#edgeCases")
        @DisplayName("Negatives, zeros, duplicates, positions, limits")
        void findsThePair(int[] nums, int target, int[] expected) {
            assertIndices(expected, twoSum.findIndices(nums, target));
        }

        @Test
        @DisplayName("Never pairs an element with itself")
        void doesNotUseTheSameElementTwice() {
            // 3 + 3 == 6 would be index 0 twice; the only valid pair is 2 + 4
            assertIndices(new int[]{1, 2}, twoSum.findIndices(new int[]{3, 2, 4}, 6));
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
            int[] nums = {2, 7, 11, 15};
            int[] snapshot = nums.clone();

            twoSum.findIndices(nums, 9);

            assertArrayEquals(snapshot, nums, "Caller's array was mutated");
        }

        @Test
        @DisplayName("No pair adds up to the target -> IllegalArgumentException")
        void noPairThrows() {
            assertThrows(IllegalArgumentException.class, () -> twoSum.findIndices(new int[]{3, 2, 4}, 10));
        }

        @Test
        @DisplayName("Fewer than two elements -> IllegalArgumentException")
        void tooFewElementsThrows() {
            assertThrows(IllegalArgumentException.class, () -> twoSum.findIndices(new int[]{5}, 10));
            assertThrows(IllegalArgumentException.class, () -> twoSum.findIndices(new int[]{}, 0));
        }

        @Test
        @DisplayName("Null input throws NullPointerException")
        void nullInputThrows() {
            assertThrows(NullPointerException.class, () -> twoSum.findIndices(null, 0));
        }

        @Test
        @DisplayName("Returns a valid pair on 1,000 random arrays")
        void returnsAValidPairOnRandomInput() {
            Random random = new Random(42); // fixed seed -> reproducible failures
            for (int run = 0; run < 1_000; run++) {
                int[] nums = random.ints(2 + random.nextInt(50), -1_000, 1_000).toArray();
                int i = random.nextInt(nums.length);
                int j = (i + 1 + random.nextInt(nums.length - 1)) % nums.length;   // j != i
                int target = nums[i] + nums[j];

                int[] result = twoSum.findIndices(nums.clone(), target);

                // Several pairs may add up to the target, so check the pair is valid, not which one it is
                String context = Arrays.toString(nums) + ", target " + target;
                assertEquals(2, result.length, () -> "Result length for " + context);
                assertNotEquals(result[0], result[1], () -> "Same index used twice for " + context);
                assertEquals(target, nums[result[0]] + nums[result[1]], () -> "Wrong pair for " + context);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Test cases: {nums, target, expected indices}
    // ---------------------------------------------------------------------

    static Stream<Arguments> exampleCases() {
        return Stream.of(
                Arguments.of(new int[]{2, 7, 11, 15}, 9, new int[]{0, 1}),     // Example 1
                Arguments.of(new int[]{3, 2, 4}, 6, new int[]{1, 2}),          // Example 2
                Arguments.of(new int[]{3, 3}, 6, new int[]{0, 1})              // Example 3
        );
    }

    static Stream<Arguments> edgeCases() {
        return Stream.of(
                Arguments.of(new int[]{-3, 4, 3, 90}, 0, new int[]{0, 2}),                          // negative + positive = 0
                Arguments.of(new int[]{-1, -2, -3, -4, -5}, -8, new int[]{2, 4}),                   // all negative
                Arguments.of(new int[]{0, 4, 3, 0}, 0, new int[]{0, 3}),                            // zeros
                Arguments.of(new int[]{1, 5, 5, 11}, 10, new int[]{1, 2}),                          // duplicate values
                Arguments.of(new int[]{1, 2, 3, 4, 5, 6}, 11, new int[]{4, 5}),                     // pair at the end
                Arguments.of(new int[]{-1_000_000_000, 5, 1_000_000_000, 0}, 1_000_000_000,
                        new int[]{2, 3}),                                                           // complement reaches 2 * 10^9
                Arguments.of(new int[]{-1_000_000_000, 7, 1_000_000_000}, 0, new int[]{0, 2})       // both limits
        );
    }

    // ---------------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------------

    /** Indices may come back in any order, so compare them sorted. */
    private static void assertIndices(int[] expected, int[] actual) {
        int[] sorted = actual.clone();
        Arrays.sort(sorted);
        assertArrayEquals(expected, sorted, () -> "Indices " + Arrays.toString(actual));
    }
}
