package dev.ravikamble.collectionsandstreams.fruitvegseparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Contract tests for {@link FruitVegSeparatorProblem}.
 *
 * Every test runs against every implementation listed in implementations().
 * To test a new solution, implement the interface and add one line there.
 *
 * Where the problem statement leaves behaviour open (null input, bad format,
 * normalization), the tests accept every reasonable choice — fail fast, skip,
 * or keep — but never silently corrupt or lose data.
 */
@DisplayName("#003 — Fruit / Vegetable Separator")
@ParameterizedClass(name = "{0}")
@MethodSource("implementations")
class FruitVegSeparatorProblemTest {

    static List<Arguments> implementations() {
        return List.of(
                Arguments.of(Named.of("SplitAndGroupBySolution", new SplitAndGroupBySolution()))
                // Arguments.of(Named.of("YourNextSolution", new YourNextSolution()))
        );
    }

    @Parameter
    FruitVegSeparatorProblem separator;

    // ---------------------------------------------------------------------
    // Scenarios: every solution must produce exactly this
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Scenarios")
    class Scenarios {

        @Test
        @DisplayName("S1: Mixed list is grouped into Fruit and Vegetable")
        void mixedList() {
            List<String> input = List.of("Fruit:Apple", "Vegetable:Potato", "Fruit:Banana", "Vegetable:Onion");

            assertEquals(Map.of(
                    "Fruit", List.of("Apple", "Banana"),
                    "Vegetable", List.of("Potato", "Onion")), separator.separate(input));
        }

        @Test
        @DisplayName("S2: Only fruits -> Fruit bucket populated, Vegetable empty or absent")
        void onlyFruits() {
            Map<String, List<String>> result = separator.separate(List.of("Fruit:Apple", "Fruit:Mango"));

            assertEquals(List.of("Apple", "Mango"), result.get("Fruit"));
            assertTrue(result.getOrDefault("Vegetable", List.of()).isEmpty());
        }

        @Test
        @DisplayName("S3: Only vegetables -> Vegetable bucket populated, Fruit empty or absent")
        void onlyVegetables() {
            Map<String, List<String>> result = separator.separate(List.of("Vegetable:Potato", "Vegetable:Onion"));

            assertEquals(List.of("Potato", "Onion"), result.get("Vegetable"));
            assertTrue(result.getOrDefault("Fruit", List.of()).isEmpty());
        }

        @Test
        @DisplayName("S4: Single entry")
        void singleEntry() {
            assertEquals(List.of("Apple"), separator.separate(List.of("Fruit:Apple")).get("Fruit"));
        }

        @Test
        @DisplayName("S5: Names keep input order within each bucket")
        void encounterOrderPreserved() {
            List<String> input = List.of("Fruit:Mango", "Fruit:Apple", "Fruit:Zucchini", "Fruit:Banana");

            assertEquals(List.of("Mango", "Apple", "Zucchini", "Banana"), separator.separate(input).get("Fruit"));
        }

        @Test
        @DisplayName("S6: Duplicates are kept")
        void duplicatesKept() {
            List<String> input = List.of("Fruit:Apple", "Fruit:Apple", "Vegetable:Potato");

            Map<String, List<String>> result = separator.separate(input);

            assertEquals(List.of("Apple", "Apple"), result.get("Fruit"));
            assertEquals(List.of("Potato"), result.get("Vegetable"));
        }

        @Test
        @Timeout(5)
        @DisplayName("S7: 1M entries processed within 5 seconds")
        void largeInput() {
            List<String> input = IntStream.range(0, 1_000_000)
                    .mapToObj(i -> i % 2 == 0 ? "Fruit:F" + i : "Vegetable:V" + i)
                    .toList();

            Map<String, List<String>> result = separator.separate(input);

            assertEquals(500_000, result.get("Fruit").size());
            assertEquals(500_000, result.get("Vegetable").size());
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: input list (fail fast or skip, never corrupt)
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: input list")
    class InputListEdgeCases {

        @Test
        @DisplayName("E1: Null list -> throws, or returns empty map")
        void nullList() {
            assertThrowsOrReturnsOneOf(null, List.of(Map.of()));
        }

        @Test
        @DisplayName("E2: Empty list -> empty map (or empty buckets)")
        void emptyList() {
            Map<String, List<String>> result = separator.separate(List.of());

            assertTrue(result.values().stream().allMatch(List::isEmpty));
        }

        @Test
        @DisplayName("E3: Null element -> throws, or skips it")
        void nullElement() {
            assertThrowsOrReturnsOneOf(Arrays.asList("Fruit:Apple", null),
                    List.of(Map.of("Fruit", List.of("Apple"))));
        }

        @Test
        @DisplayName("E4: Empty and blank strings -> throws, or skips them")
        void emptyAndBlankStrings() {
            assertThrowsOrReturnsOneOf(List.of("Fruit:Apple", ""),
                    List.of(Map.of("Fruit", List.of("Apple"))));
            assertThrowsOrReturnsOneOf(List.of("Fruit:Apple", "  "),
                    List.of(Map.of("Fruit", List.of("Apple"))));
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: "Type:Name" format
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: Type:Name format")
    class FormatEdgeCases {

        @Test
        @DisplayName("E5: No delimiter -> throws, or skips the entry")
        void noDelimiter() {
            assertThrowsOrReturnsOneOf(List.of("Fruit:Apple", "Potato"),
                    List.of(Map.of("Fruit", List.of("Apple"))));
        }

        @Test
        @DisplayName("E6: Missing name -> throws, skips, or keeps empty name")
        void missingName() {
            assertThrowsOrReturnsOneOf(List.of("Fruit:"),
                    List.of(Map.of(), Map.of("Fruit", List.of(""))));
        }

        @Test
        @DisplayName("E7: Missing type -> throws, skips, or keeps empty key")
        void missingType() {
            assertThrowsOrReturnsOneOf(List.of(":Apple"),
                    List.of(Map.of(), Map.of("", List.of("Apple"))));
        }

        @Test
        @DisplayName("E8: Extra delimiters stay in the name")
        void extraDelimiters() {
            assertEquals(List.of("Apple:Red"), separator.separate(List.of("Fruit:Apple:Red")).get("Fruit"));
        }

        @Test
        @DisplayName("E9: Whitespace -> kept as-is, or trimmed")
        void whitespace() {
            assertThrowsOrReturnsOneOf(List.of(" Fruit : Apple "),
                    List.of(Map.of(" Fruit ", List.of(" Apple ")),
                            Map.of("Fruit", List.of("Apple"))));
        }

        @Test
        @DisplayName("E10: Case variants -> separate keys, or merged into one")
        void caseVariants() {
            Map<String, List<String>> result =
                    separator.separate(List.of("Fruit:Apple", "fruit:Mango", "FRUIT:Grapes"));

            if (result.size() == 1) {
                assertEquals(List.of("Apple", "Mango", "Grapes"), result.values().iterator().next());
            } else {
                assertEquals(Map.of(
                        "Fruit", List.of("Apple"),
                        "fruit", List.of("Mango"),
                        "FRUIT", List.of("Grapes")), result);
            }
        }

        @Test
        @DisplayName("E11: Spelling variants -> separate keys, or merged into one")
        void spellingVariants() {
            Map<String, List<String>> result =
                    separator.separate(List.of("Vegitable:Potato", "Vegetable:Onion"));

            if (result.size() == 1) {
                assertEquals(List.of("Potato", "Onion"), result.values().iterator().next());
            } else {
                assertEquals(Map.of(
                        "Vegitable", List.of("Potato"),
                        "Vegetable", List.of("Onion")), result);
            }
        }

        @Test
        @DisplayName("E12: Unknown type -> own key, ignored, or rejected")
        void unknownType() {
            assertThrowsOrReturnsOneOf(List.of("Fruit:Apple", "Nut:Almond"),
                    List.of(Map.of("Fruit", List.of("Apple"), "Nut", List.of("Almond")),
                            Map.of("Fruit", List.of("Apple"))));
        }

        @Test
        @DisplayName("E13: Different delimiter -> throws, or skips the entry")
        void differentDelimiter() {
            assertThrowsOrReturnsOneOf(List.of("Fruit-Apple"), List.of(Map.of()));
            assertThrowsOrReturnsOneOf(List.of("Fruit=Apple"), List.of(Map.of()));
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: output
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: output")
    class OutputEdgeCases {

        @Test
        @DisplayName("E14: Input list is not mutated")
        void inputNotMutated() {
            List<String> input = new ArrayList<>(List.of("Fruit:Apple", "Vegetable:Potato"));
            List<String> snapshot = List.copyOf(input);

            separator.separate(input);

            assertEquals(snapshot, input);
        }

        @Test
        @DisplayName("E14: Works with an immutable input list")
        void immutableInputAccepted() {
            Map<String, List<String>> result = separator.separate(List.of("Fruit:Apple"));

            assertFalse(result.isEmpty());
        }
    }

    // ---------------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------------

    /**
     * Passes if the solution either throws a RuntimeException (fail fast)
     * or returns one of the allowed results (skip / keep / normalize).
     * Empty buckets are ignored, so {Fruit=[]} counts the same as {}.
     */
    private void assertThrowsOrReturnsOneOf(List<String> input, List<Map<String, List<String>>> allowed) {
        Map<String, List<String>> result;
        try {
            result = separator.separate(input);
        } catch (RuntimeException e) {
            return;   // fail fast is an accepted choice
        }

        Map<String, List<String>> withoutEmptyBuckets = new java.util.HashMap<>(result);
        withoutEmptyBuckets.values().removeIf(List::isEmpty);

        if (!allowed.contains(withoutEmptyBuckets)) {
            fail("Input " + input + " returned " + result + ", expected an exception or one of " + allowed);
        }
    }
}