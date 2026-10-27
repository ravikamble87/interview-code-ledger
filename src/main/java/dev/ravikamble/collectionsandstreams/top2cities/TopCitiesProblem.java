package dev.ravikamble.collectionsandstreams.top2cities;

import java.util.List;
import java.util.Map;

/**
 * <h2>Problem #006 — Top Cities by Successful Transaction Amount</h2>
 *
 * <p>Given a list of {@link Transaction}s ({@code id, amount, status, city}), find the
 * {@value #TOP_N} cities with the highest <b>total amount of successful transactions</b>,
 * highest total first.
 *
 * <h3>Example 1</h3>
 * <pre>
 * Input (all SUCCESS):
 *   Mumbai 1.0T, Mumbai 0.8T, Bangalore 1.5T, Pune 0.7T, Pune 0.6T, Nagpur 0.5T
 * Output:
 *   { Mumbai=1.8T, Bangalore=1.5T, Pune=1.3T, Nagpur=0.5T }
 * </pre>
 *
 * <h3>Example 2 — only SUCCESS counts</h3>
 * <pre>
 * Input  : Mumbai 1.0M SUCCESS, Mumbai 5.0M FAILED, Pune 0.9M PENDING, Pune 0.4M SUCCESS
 * Output : { Mumbai=1.0M, Pune=0.4M }
 * </pre>
 *
 * <h3>Rules (the contract every implementation must meet)</h3>
 * <ol>
 *   <li>Only transactions whose status is {@code SUCCESS} count. The status is compared
 *       ignoring case and surrounding spaces, so {@code "success"} and {@code " Success "} count too.</li>
 *   <li>Rows with a {@code null} amount, status or city are skipped; they never cause an exception.</li>
 *   <li>Cities are grouped ignoring case and surrounding spaces ({@code "Pune"} and {@code "pune "}
 *       are one city). The key returned is the city name as it <b>first</b> appears, trimmed.</li>
 *   <li>At most {@value #TOP_N} cities are returned, highest total first.</li>
 *   <li>Cities with equal totals are ordered alphabetically (ignoring case), so the result is deterministic.</li>
 *   <li>The input list is not modified. No qualifying transaction (including an empty list) gives an empty map.</li>
 * </ol>
 *
 * <h3>Constraints</h3>
 * <ul>
 *   <li>0 &le; transactions.size() &le; 10<sup>6</sup></li>
 *   <li>The list itself is never {@code null}.</li>
 * </ul>
 *
 * <h3>Edge cases</h3>
 * <ul>
 *   <li>Fewer than {@value #TOP_N} cities → all of them</li>
 *   <li>A city with only FAILED/PENDING transactions → not in the result at all</li>
 *   <li>Ties on the total → alphabetical</li>
 *   <li>{@code null} fields, mixed case, extra spaces (rules 1–3)</li>
 *   <li>Decimal amounts: {@code 0.1 + 0.2} is {@code 0.30000000000000004} with {@code double}</li>
 * </ul>
 *
 * <h3>Follow-up</h3>
 * <ul>
 *   <li>Amounts are {@code Double} here. Why would production code use {@code BigDecimal}?</li>
 *   <li>Can you find the top N without sorting every city (better than O(k log k))?
 *       Hint: a heap of size N.</li>
 *   <li>Why does the method return a result instead of printing it?</li>
 * </ul>
 *
 * <h3>Solutions</h3>
 * <ul>
 *   <li>{@link Solution1} — one stream: filter, group with a merge, sort, limit</li>
 * </ul>
 *
 * <h3>Source</h3>
 * <p>Asked in: Technical round, Oct 2026
 *
 * @author Ravi Kamble
 */
public interface TopCitiesProblem {

    /** Number of cities to return. */
    int TOP_N = 10;

    /**
     * Returns the top {@value #TOP_N} cities by total successful transaction amount.
     *
     * @param transactions the transactions to aggregate; never {@code null}, may be empty; not modified
     * @return city to total, iterating from the highest total to the lowest; empty if nothing qualifies
     */
    Map<String, Double> topCities(List<Transaction> transactions);

    /**
     * Prints {@link #topCities(List)} one city per line as {@code City: total}.
     *
     * <p>Kept separate so solutions never print and tests check the returned value
     * (see "No printing in solutions" in the README).
     *
     * @param transactions the transactions to aggregate; never {@code null}, may be empty
     */
    default void printTopCities(List<Transaction> transactions) {
        topCities(transactions).forEach((city, total) -> System.out.println(city + ": " + total));
    }
}
