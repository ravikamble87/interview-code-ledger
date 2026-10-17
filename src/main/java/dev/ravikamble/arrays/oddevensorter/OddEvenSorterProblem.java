package dev.ravikamble.arrays.oddevensorter;

/**
 * <h2>Problem #001 — Sort Odds Descending, Evens Ascending</h2>
 *
 * <p>Given an array of integers, rearrange it so that:
 * <ul>
 *   <li>all <b>odd</b> numbers come first, sorted in <b>descending</b> order</li>
 *   <li>all <b>even</b> numbers follow, sorted in <b>ascending</b> order</li>
 * </ul>
 *
 * <h3>Example 1</h3>
 * <pre>
 * Input  : [12, 1, 8, 6, 7, 9, 10, 5, 6]
 * Output : [9, 7, 5, 1, 6, 6, 8, 10, 12]
 *
 * Odds  : 1, 7, 9, 5        → desc → 9, 7, 5, 1
 * Evens : 12, 8, 6, 10, 6   → asc  → 6, 6, 8, 10, 12
 * </pre>
 *
 * <h3>Example 2 — negatives and zero</h3>
 * <pre>
 * Input  : [-3, 4, 0, -7, 2, 5]
 * Output : [5, -3, -7, 0, 2, 4]
 * </pre>
 *
 * <h3>Example 3 — only one parity</h3>
 * <pre>
 * Input  : [3, 11, 1]      Output : [11, 3, 1]
 * Input  : [8, 2, 4]       Output : [2, 4, 8]
 * </pre>
 *
 * <h3>Edge cases</h3>
 * <ul>
 *   <li>Empty array → empty array</li>
 *   <li>Single element → same values</li>
 *   <li>Duplicates are kept (e.g. 6 appears twice in Example 1)</li>
 *   <li>Zero is even</li>
 *   <li>Negative numbers: parity check must use {@code n % 2 != 0},
 *       since {@code -3 % 2 == -1} in Java</li>
 * </ul>
 *
 * <h3>Follow-up asked</h3>
 * <p>Keep odd and even numbers in their <b>original positions</b>,
 * sorting each group only within its own slots.
 * <pre>
 * Input  : [12, 1, 8, 6, 7, 9, 10, 5, 6]
 * Output : [6, 9, 6, 8, 7, 5, 10, 1, 12]
 * </pre>
 *
 * <h3>Solutions</h3>
 * <ul>
 *   <li>{@link PartitionSolution} — general; handles any integer</li>
 *   <li>{@link NegationSolution} — non-negative input only</li>
 * </ul>
 *
 * <h3>Source</h3>
 * <p>Asked in: Technical round (Fintech), Oct 2026
 *
 * @author Ravi Kamble
 */
public interface OddEvenSorterProblem {

    /**
     * Returns a new array with odd numbers first in descending order,
     * followed by even numbers in ascending order.
     *
     * <p>Implementations must <b>not</b> modify {@code input}.
     *
     * @param input the integers to sort; may be empty
     * @return a new array containing the same values, rearranged
     * @throws NullPointerException if {@code input} is {@code null}
     */
    int[] sort(int[] input);
}