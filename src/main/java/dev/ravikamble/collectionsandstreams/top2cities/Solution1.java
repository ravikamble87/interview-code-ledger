package dev.ravikamble.collectionsandstreams.top2cities;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <b>Approach — One stream: filter, group with a merge, sort, limit.</b>
 *
 * <ol>
 *   <li>Drop rows with a {@code null} amount, status or city, then keep only {@code SUCCESS}
 *       (trimmed, case-insensitive).</li>
 *   <li>Group by the normalised city (trimmed, lower case) with {@code toMap}. The value is a small
 *       {@link CityTotal} holding the city's first spelling and its running total; the merge
 *       function adds amounts and keeps the first spelling. {@link LinkedHashMap} keeps encounter
 *       order, so "first" really is the first row.</li>
 *   <li>Sort the totals: highest first, then by name (case-insensitive) to break ties.</li>
 *   <li>Keep the first {@value TopCitiesProblem#TOP_N} and collect into a {@link LinkedHashMap},
 *       which keeps the sorted order. A plain {@code Collectors.toMap} would return a
 *       {@code HashMap} and lose it.</li>
 * </ol>
 *
 * <p><b>Limitations:</b> amounts are summed as {@code double}, so decimal totals can be off in
 * the last digits (e.g. {@code 0.1 + 0.2}).
 *
 * <p><b>Time:</b> O(n + k log k), n = transactions, k = distinct cities &nbsp;
 * <b>Space:</b> O(k) for the city totals
 *
 * @author Ravi Kamble
 */
public class Solution1 implements TopCitiesProblem {

    private static final String SUCCESS = "SUCCESS";

    /** A city's display name (first spelling seen, trimmed) and its running total. */
    private record CityTotal(String city, double total) {

        CityTotal plus(CityTotal other) {
            return new CityTotal(city, total + other.total);   // keep the first spelling
        }
    }

    @Override
    public Map<String, Double> topCities(List<Transaction> transactions) {
        return transactions.stream()
                // 1. skip incomplete rows, keep only successful transactions
                .filter(t -> t.amount() != null && t.status() != null && t.city() != null)
                .filter(t -> SUCCESS.equalsIgnoreCase(t.status().strip()))
                // 2. normalised city -> (first spelling, total)
                .collect(Collectors.toMap(
                        t -> t.city().strip().toLowerCase(Locale.ROOT),
                        t -> new CityTotal(t.city().strip(), t.amount()),
                        CityTotal::plus,
                        LinkedHashMap::new))
                .values().stream()
                // 3. highest total first; ties alphabetical
                .sorted(Comparator.comparingDouble(CityTotal::total).reversed()
                        .thenComparing(CityTotal::city, String.CASE_INSENSITIVE_ORDER))
                // 4. top N, keeping the sorted order
                .limit(TOP_N)
                .collect(Collectors.toMap(
                        CityTotal::city,
                        CityTotal::total,
                        (a, b) -> a,          // never happens: cities are already unique
                        LinkedHashMap::new));
    }
}
