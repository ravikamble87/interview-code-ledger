package dev.ravikamble.arrays.longestconsecutive;

/**
 * <h2>Problem #002 — Longest Consecutive Sequence</h2>
 *
 * <p>Given an <b>unsorted</b> array of integers, return the <b>length</b> of the
 * longest run of consecutive integers (each value exactly 1 more than the previous).
 * <ul>
 *   <li>The values do <b>not</b> need to be next to each other in the array</li>
 *   <li>Order in the array does not matter, only which values are present</li>
 *   <li>Return the length (a count), not the sequence itself</li>
 * </ul>
 *
 * <h3>Example 1 — happy path</h3>
 * <pre>
 * Input  : [100, 4, 200, 1, 3, 2]
 * Output : 4
 *
 * Runs present : 1, 2, 3, 4   → length 4   ← longest
 *                100          → length 1
 *                200          → length 1
 * </pre>
 *
 * <h3>Example 2 — longer run, scattered across the array</h3>
 * <pre>
 * Input  : [0, 3, 7, 2, 5, 8, 4, 6, 0, 1]
 * Output : 9
 *
 * Run : 0, 1, 2, 3, 4, 5, 6, 7, 8   → length 9
 * (0 appears twice but counts once)
 * </pre>
 *
 * <h3>Example 3 — two competing runs</h3>
 * <pre>
 * Input  : [10, 5, 12, 3, 11, 4, 13]
 * Output : 4
 *
 * Runs : 10, 11, 12, 13   → length 4   ← longest
 *        3, 4, 5          → length 3
 * </pre>
 *
 * <h3>Example 4 — negatives and zero</h3>
 * <pre>
 * Input  : [-1, -2, 0, 1, 9]
 * Output : 4      (-2, -1, 0, 1)
 * </pre>
 *
 * <h3>Edge cases</h3>
 * <ul>
 *   <li>Empty array → {@code 0}</li>
 *   <li>Single element → {@code 1}</li>
 *   <li>All values the same, e.g. {@code [5, 5, 5]} → {@code 1}</li>
 *   <li>Duplicates inside a run count once, e.g. {@code [1, 2, 0, 1]} → {@code 3}</li>
 *   <li>No two values consecutive, e.g. {@code [10, 30, 20]} → {@code 1}</li>
 *   <li>Already sorted input, e.g. {@code [1, 2, 3, 4, 5]} → {@code 5}</li>
 *   <li>Negative numbers and zero work like any other integer</li>
 *   <li><b>Integer overflow</b>: {@code [Integer.MAX_VALUE, Integer.MIN_VALUE]} → {@code 1}.
 *       In Java, {@code Integer.MAX_VALUE + 1} wraps to {@code Integer.MIN_VALUE},
 *       so a naive {@code n + 1} check would wrongly report {@code 2}</li>
 * </ul>
 *
 * <h3>Constraint usually asked</h3>
 * <p>Solve it in <b>O(n)</b> time. Sorting first works, but it is O(n log n).
 *
 * <h3>Solutions</h3>
 * <ul>
 *   <li>{@link SortingSolution} — sort, then scan once; O(n log n)</li>
 *   <li>{@link HashSetSolution} — HashSet, count from the start of each run; O(n) expected</li>
 * </ul>
 *
 * <h3>Source</h3>
 * <p>Asked in: Technical round, Oct 2026
 *
 * @author Ravi Kamble
 */
public interface LongestConsecutiveSequenceProblem {

    /**
     * Returns the length of the longest run of consecutive integers
     * present in {@code input}, in any order.
     *
     * <p>Implementations must <b>not</b> modify {@code input}.
     *
     * @param input the integers to examine; may be empty, may contain duplicates
     * @return length of the longest consecutive run; {@code 0} if {@code input} is empty
     * @throws NullPointerException if {@code input} is {@code null}
     */
    int longestConsecutive(int[] input);
}