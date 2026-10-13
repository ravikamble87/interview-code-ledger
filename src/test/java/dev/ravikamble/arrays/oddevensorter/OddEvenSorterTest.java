package dev.ravikamble.arrays.oddevensorter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
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

@DisplayName("#001 — Sort Odds Descending, Evens Ascending")
class OddEvenSorterTest {

    // ---------------------------------------------------------------
    // Register solutions here. Contributors add one line.
    // ---------------------------------------------------------------

    /** Solutions that must handle ANY integer, including negatives. */
    private static final List<NegationSolution> GENERAL_SOLUTIONS = List.of(
            new NegationSolution()
    );

    /** Solutions documented as valid only for non-negative input. */
    private static final List<NegationSolution> NON_NEGATIVE_ONLY_SOLUTIONS = List.of(
            new NegationSolution()
    );

    // ---------------------------------------------------------------
    // Test cases: {input, expected}
    // ---------------------------------------------------------------

    private static final List<int[][]> NON_NEGATIVE_CASES = List.of(
            new int[][]{{12, 1, 8, 6, 7, 9, 10, 5, 6}, {9, 7, 5, 1, 6, 6, 8, 10, 12}}, // Example 1
            new int[][]{{3, 11, 1}, {11, 3, 1}},                                     // only odds
            new int[][]{{8, 2, 4}, {2, 4, 8}},                                       // only evens
            new int[][]{{}, {}},                                                     // empty
            new int[][]{{7}, {7}},                                                   // single odd
            new int[][]{{2}, {2}},                                                   // single even
            new int[][]{{0, 1}, {1, 0}},                                             // zero is even
            new int[][]{{5, 2, 5, 2}, {5, 5, 2, 2}},                                 // duplicates
            new int[][]{{9, 7, 2, 4}, {9, 7, 2, 4}}                                  // already sorted
    );

    private static final List<int[][]> NEGATIVE_CASES = List.of(
            new int[][]{{-3, 4, 0, -7, 2, 5}, {5, -3, -7, 0, 2, 4}},                 // Example 2
            new int[][]{{-4, 3}, {3, -4}},                                           // negative even
            new int[][]{{-1, -2, -3, -4}, {-1, -3, -4, -2}},                         // all negative
            new int[][]{{Integer.MIN_VALUE, Integer.MAX_VALUE},                      // int extremes
                    {Integer.MAX_VALUE, Integer.MIN_VALUE}}
    );

    // ---------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------

    @ParameterizedTest(name = "{0}: {1} → {2}")
    @MethodSource("nonNegativeInputs")
    void sortsNonNegativeInput(NegationSolution sorter, int[] input, int[] expected) {
        assertArrayEquals(expected, sorter.sort(input));
    }

    @ParameterizedTest(name = "{0}: {1} → {2}")
    @MethodSource("negativeInputs")
    void sortsInputWithNegatives(NegationSolution sorter, int[] input, int[] expected) {
        assertArrayEquals(expected, sorter.sort(input));
    }

    @ParameterizedTest(name = "{0} does not modify its input")
    @MethodSource("allSolutions")
    void doesNotModifyInput(NegationSolution sorter) {
        int[] input = {12, 1, 8, 6, 7, 9, 10, 5, 6};
        int[] snapshot = input.clone();

        sorter.sort(input);

        assertArrayEquals(snapshot, input, "Caller's array was mutated");
    }

    @ParameterizedTest(name = "{0} matches reference on 1,000 random arrays")
    @MethodSource("generalSolutions")
    void matchesReferenceOnRandomInput(NegationSolution sorter) {
        Random random = new Random(42); // fixed seed → reproducible failures
        for (int run = 0; run < 1_000; run++) {
            int[] input = random.ints(random.nextInt(50), -1_000, 1_000).toArray();
            assertArrayEquals(reference(input), sorter.sort(input.clone()),
                    () -> "Failed for input " + Arrays.toString(input));
        }
    }

    // ---------------------------------------------------------------
    // Argument sources
    // ---------------------------------------------------------------

    static Stream<Arguments> nonNegativeInputs() {
        return combine(Stream.concat(GENERAL_SOLUTIONS.stream(),
                        NON_NEGATIVE_ONLY_SOLUTIONS.stream()),
                NON_NEGATIVE_CASES);
    }

    static Stream<Arguments> negativeInputs() {
        return combine(GENERAL_SOLUTIONS.stream(), NEGATIVE_CASES);
    }

    static Stream<Named<NegationSolution>> allSolutions() {
        return Stream.concat(GENERAL_SOLUTIONS.stream(), NON_NEGATIVE_ONLY_SOLUTIONS.stream())
                .map(OddEvenSorterTest::named);
    }

    static Stream<Named<NegationSolution>> generalSolutions() {
        return GENERAL_SOLUTIONS.stream().map(OddEvenSorterTest::named);
    }

    private static Stream<Arguments> combine(Stream<NegationSolution> sorters, List<int[][]> cases) {
        return sorters.flatMap(sorter -> cases.stream()
                .map(c -> Arguments.of(named(sorter), c[0].clone(), c[1])));
    }

    private static Named<NegationSolution> named(NegationSolution sorter) {
        return Named.of(sorter.getClass().getSimpleName(), sorter);
    }

    // ---------------------------------------------------------------
    // Test oracle: deliberately simple, obviously correct, not optimised
    // ---------------------------------------------------------------

    private static int[] reference(int[] input) {
        return IntStream.concat(
                Arrays.stream(input).filter(n -> n % 2 != 0).boxed()
                        .sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue),
                Arrays.stream(input).filter(n -> n % 2 == 0).sorted()
        ).toArray();
    }
}