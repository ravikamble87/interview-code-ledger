package dev.ravikamble.collectionsandstreams.top2cities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contract tests for {@link TopCitiesProblem}.
 *
 * Every test runs against every implementation listed in implementations().
 * To test a new solution, implement the interface and add one line there.
 *
 * Results are compared in iteration order (city and total), because the
 * order is part of the contract. Totals are doubles, so they are compared
 * with a small tolerance.
 */
@DisplayName("#006 — Top Cities by Successful Transaction Amount")
@ParameterizedClass(name = "{0}")
@MethodSource("implementations")
class TopCitiesProblemTest {

    private static final double DELTA = 1e-6;

    static List<Arguments> implementations() {
        return List.of(
                Arguments.of(Named.of("Solution1", new Solution1())),
                Arguments.of(Named.of("Solution2", new Solution2()))
        );
    }

    @Parameter
    TopCitiesProblem topCities;

    // ---------------------------------------------------------------------
    // Scenarios
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Scenarios")
    class Scenarios {

        @Test
        @DisplayName("Example 1: fewer than TOP_N cities -> all of them, highest first")
        void returnsAllCitiesHighestFirst() {
            Map<String, Double> result = topCities.topCities(List.of(
                    tx("T01", 1.0e12, "SUCCESS", "Mumbai"),
                    tx("T02", 0.8e12, "SUCCESS", "Mumbai"),
                    tx("T03", 1.5e12, "SUCCESS", "Bangalore"),
                    tx("T04", 0.7e12, "SUCCESS", "Pune"),
                    tx("T05", 0.6e12, "SUCCESS", "Pune"),
                    tx("T06", 0.5e12, "SUCCESS", "Nagpur")));

            assertResult(result,
                    "Mumbai", 1.8e12,
                    "Bangalore", 1.5e12,
                    "Pune", 1.3e12,
                    "Nagpur", 0.5e12);
        }

        @Test
        @DisplayName("13 cities -> only the top TOP_N")
        void returnsOnlyTheTopN() {
            Map<String, Double> result = topCities.topCities(thirteenCitiesWithMixedStatuses());

            assertEquals(TopCitiesProblem.TOP_N, result.size(), "number of cities");
            assertResult(result,
                    "Mumbai", 1_800_000,
                    "Bangalore", 1_500_000,
                    "Pune", 1_300_000,
                    "Delhi", 1_200_000,
                    "Hyderabad", 1_100_000,
                    "Chennai", 1_000_000,
                    "Kolkata", 950_000,
                    "Ahmedabad", 900_000,
                    "Jaipur", 850_000,
                    "Lucknow", 800_000);
        }

        @Test
        @DisplayName("Example 2: FAILED and PENDING amounts are not counted")
        void ignoresNonSuccessfulTransactions() {
            Map<String, Double> result = topCities.topCities(thirteenCitiesWithMixedStatuses());

            assertEquals(1_800_000, result.get("Mumbai"), DELTA, "Mumbai without its FAILED 5,000,000");
            assertEquals(1_300_000, result.get("Pune"), DELTA, "Pune without its PENDING 900,000");
            assertFalse(result.containsKey("Surat"), "Surat has only a FAILED transaction");
        }
    }

    // ---------------------------------------------------------------------
    // Edge cases: data
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Edge cases: data")
    class DataEdgeCases {

        @Test
        @DisplayName("Empty list -> empty map")
        void emptyInput() {
            assertTrue(topCities.topCities(List.of()).isEmpty());
        }

        @Test
        @DisplayName("No successful transaction -> empty map")
        void noSuccessfulTransaction() {
            Map<String, Double> result = topCities.topCities(List.of(
                    tx("T01", 1_000_000.0, "FAILED", "Pune"),
                    tx("T02", 500_000.0, "PENDING", "Mumbai")));

            assertTrue(result.isEmpty(), () -> "Expected no cities but got " + result);
        }

        @Test
        @DisplayName("Equal totals -> alphabetical order")
        void breaksTiesAlphabetically() {
            Map<String, Double> result = topCities.topCities(List.of(
                    tx("T01", 600_000.0, "SUCCESS", "Pune"),
                    tx("T02", 400_000.0, "SUCCESS", "Pune"),
                    tx("T03", 1_000_000.0, "SUCCESS", "Mumbai"),
                    tx("T04", 200_000.0, "SUCCESS", "Delhi")));

            assertResult(result,
                    "Mumbai", 1_000_000,
                    "Pune", 1_000_000,
                    "Delhi", 200_000);
        }

        @Test
        @DisplayName("Null fields skipped; status and city ignore case and spaces; first spelling kept")
        void skipsNullsAndNormalisesStatusAndCity() {
            List<Transaction> messy = List.of(
                    tx("T01", 500_000.0, "SUCCESS", "Pune"),
                    tx("T02", 300_000.0, "success", "Pune"),     // lower-case status
                    tx("T03", 200_000.0, " Success ", "Pune"),   // padded status
                    tx("T04", null, "SUCCESS", "Pune"),          // null amount -> skipped
                    tx("T05", 900_000.0, "SUCCESS", null),       // null city   -> skipped
                    tx("T06", 800_000.0, null, "Mumbai"),        // null status -> skipped
                    tx("T07", 300_000.0, "SUCCESS", "Mumbai"),
                    tx("T08", 200_000.0, "SUCCESS", "pune "));   // same city as "Pune"

            Map<String, Double> result = assertDoesNotThrow(() -> topCities.topCities(messy));

            assertResult(result,
                    "Pune", 1_200_000,
                    "Mumbai", 300_000);
        }

        @Test
        @DisplayName("Decimal amounts add up (within double precision)")
        void sumsDecimalAmounts() {
            Map<String, Double> result = topCities.topCities(List.of(
                    tx("T01", 0.1, "SUCCESS", "Pune"),
                    tx("T02", 0.2, "SUCCESS", "Pune")));

            assertEquals(1, result.size(), "number of cities");
            assertEquals(0.3, result.get("Pune"), DELTA, "0.1 + 0.2");
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
            List<Transaction> input = new ArrayList<>(thirteenCitiesWithMixedStatuses());
            List<Transaction> snapshot = List.copyOf(input);

            topCities.topCities(input);

            assertEquals(snapshot, input, "Caller's list was mutated");
        }

        @Test
        @DisplayName("printTopCities prints one 'City: total' line per city, in order")
        void printsTheResult() {
            List<Transaction> input = List.of(
                    tx("T01", 2.5, "SUCCESS", "Pune"),
                    tx("T02", 4.0, "SUCCESS", "Mumbai"));

            String printed = captureSystemOut(() -> topCities.printTopCities(input));

            assertEquals(List.of("Mumbai: 4.0", "Pune: 2.5"), printed.lines().toList());
        }

        @Test
        @DisplayName("Matches reference on 1,000 random transaction lists")
        void matchesReferenceOnRandomInput() {
            Random random = new Random(42); // fixed seed -> reproducible failures
            String[] cities = {"Pune", "Mumbai", "Delhi", "Chennai", "Kolkata", "Jaipur", "Surat",
                    "Indore", "Nagpur", "Lucknow", "Patna", "Bhopal", "Goa", "Agra", "Kochi"};
            String[] statuses = {"SUCCESS", "FAILED", "PENDING"};

            for (int run = 0; run < 1_000; run++) {
                List<Transaction> input = new ArrayList<>();
                for (int i = 0; i < random.nextInt(60); i++) {
                    input.add(tx("T" + i,
                            (double) random.nextInt(10_000),                 // whole numbers: exact sums
                            statuses[random.nextInt(statuses.length)],
                            cities[random.nextInt(cities.length)]));
                }

                Map<String, Double> expected = reference(input);
                Map<String, Double> actual = topCities.topCities(input);

                assertEquals(List.copyOf(expected.entrySet()), List.copyOf(actual.entrySet()),
                        () -> "Failed for input " + input);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Test data
    // ---------------------------------------------------------------------

    /**
     * 13 cities, mixed statuses. Top 10: Mumbai 1.8M, Bangalore 1.5M, Pune 1.3M, Delhi 1.2M,
     * Hyderabad 1.1M, Chennai 1.0M, Kolkata 0.95M, Ahmedabad 0.9M, Jaipur 0.85M, Lucknow 0.8M.
     * Left out: Nagpur 0.5M (11th), Indore 0.3M (12th), Surat (FAILED only).
     */
    private static List<Transaction> thirteenCitiesWithMixedStatuses() {
        return List.of(
                tx("T01", 1_000_000.0, "SUCCESS", "Mumbai"),
                tx("T02", 800_000.0, "SUCCESS", "Mumbai"),
                tx("T03", 5_000_000.0, "FAILED", "Mumbai"),
                tx("T04", 900_000.0, "SUCCESS", "Bangalore"),
                tx("T05", 600_000.0, "SUCCESS", "Bangalore"),
                tx("T06", 700_000.0, "SUCCESS", "Pune"),
                tx("T07", 400_000.0, "SUCCESS", "Pune"),
                tx("T08", 200_000.0, "SUCCESS", "Pune"),
                tx("T09", 900_000.0, "PENDING", "Pune"),
                tx("T10", 1_200_000.0, "SUCCESS", "Delhi"),
                tx("T11", 500_000.0, "SUCCESS", "Hyderabad"),
                tx("T12", 600_000.0, "SUCCESS", "Hyderabad"),
                tx("T13", 1_000_000.0, "SUCCESS", "Chennai"),
                tx("T14", 950_000.0, "SUCCESS", "Kolkata"),
                tx("T15", 900_000.0, "SUCCESS", "Ahmedabad"),
                tx("T16", 2_000_000.0, "FAILED", "Ahmedabad"),
                tx("T17", 850_000.0, "SUCCESS", "Jaipur"),
                tx("T18", 800_000.0, "SUCCESS", "Lucknow"),
                tx("T19", 500_000.0, "SUCCESS", "Nagpur"),
                tx("T20", 300_000.0, "SUCCESS", "Indore"),
                tx("T21", 3_000_000.0, "FAILED", "Surat"));
    }

    private static Transaction tx(String id, Double amount, String status, String city) {
        return new Transaction(id, amount, status, city);
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    /** Test oracle: deliberately simple loops, obviously correct, not optimised. */
    private static Map<String, Double> reference(List<Transaction> transactions) {
        Map<String, String> firstSpelling = new LinkedHashMap<>();
        Map<String, Double> totals = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            if (t.amount() == null || t.status() == null || t.city() == null
                    || !t.status().strip().equalsIgnoreCase("SUCCESS")) {
                continue;
            }
            String key = t.city().strip().toLowerCase(Locale.ROOT);
            firstSpelling.putIfAbsent(key, t.city().strip());
            totals.merge(key, t.amount(), Double::sum);
        }

        List<String> keys = new ArrayList<>(totals.keySet());
        keys.sort(Comparator.<String>comparingDouble(totals::get).reversed()
                .thenComparing(k -> firstSpelling.get(k), String.CASE_INSENSITIVE_ORDER));

        Map<String, Double> top = new LinkedHashMap<>();
        for (String key : keys.subList(0, Math.min(TopCitiesProblem.TOP_N, keys.size()))) {
            top.put(firstSpelling.get(key), totals.get(key));
        }
        return top;
    }

    /** Asserts the exact cities and totals, in iteration order. Pairs: city, total, city, total, ... */
    private static void assertResult(Map<String, Double> actual, Object... cityTotalPairs) {
        List<Map.Entry<String, Double>> entries = List.copyOf(actual.entrySet());
        assertEquals(cityTotalPairs.length / 2, entries.size(), () -> "number of cities in " + actual);
        for (int i = 0; i < entries.size(); i++) {
            String city = (String) cityTotalPairs[2 * i];
            double total = ((Number) cityTotalPairs[2 * i + 1]).doubleValue();
            int position = i + 1;
            assertEquals(city, entries.get(i).getKey(), () -> "city at position " + position + " in " + actual);
            assertEquals(total, entries.get(i).getValue(), DELTA, () -> "total for " + city);
        }
    }

    private static String captureSystemOut(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
