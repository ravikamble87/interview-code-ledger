package dev.ravikamble.arrays.twosum;

/**
 * <h2>Problem #005 — Two Sum</h2>
 *
 * <p>Given an array of integers {@code nums} and an integer {@code target}, return the
 * indices of the two numbers that add up to {@code target}.
 *
 * <p>Each input has exactly one solution, and you may not use the same element twice.
 * The indices may be returned in any order.
 *
 * <h3>Example 1</h3>
 * <pre>
 * Input  : nums = [2, 7, 11, 15], target = 9
 * Output : [0, 1]
 * Explanation: nums[0] + nums[1] == 9
 * </pre>
 *
 * <h3>Example 2</h3>
 * <pre>
 * Input  : nums = [3, 2, 4], target = 6
 * Output : [1, 2]
 * Note   : [0, 0] is wrong; the same element (3) cannot be used twice
 * </pre>
 *
 * <h3>Example 3 — duplicates</h3>
 * <pre>
 * Input  : nums = [3, 3], target = 6
 * Output : [0, 1]
 * </pre>
 *
 * <h3>Constraints</h3>
 * <ul>
 *   <li>2 &le; nums.length &le; 10<sup>4</sup></li>
 *   <li>-10<sup>9</sup> &le; nums[i] &le; 10<sup>9</sup></li>
 *   <li>-10<sup>9</sup> &le; target &le; 10<sup>9</sup></li>
 *   <li>Exactly one valid answer exists.</li>
 * </ul>
 *
 * <h3>Edge cases</h3>
 * <ul>
 *   <li>Negative numbers and zero (e.g. {@code [-3, 4, 3, 90]}, target 0)</li>
 *   <li>Duplicate values (Example 3)</li>
 *   <li>The pair is the last two elements</li>
 *   <li>Values at the constraint limits: {@code target - nums[i]} or {@code nums[i] + nums[j]} can reach
 *       2 * 10<sup>9</sup>, which still fits in an {@code int} (max about 2.147 * 10<sup>9</sup>)</li>
 *   <li>No pair exists, or fewer than two elements: outside the constraints, so the contract
 *       says to fail fast with {@link IllegalArgumentException} rather than return a fake answer</li>
 * </ul>
 *
 * <h3>Follow-up</h3>
 * <p>Can you come up with an algorithm that is better than O(n<sup>2</sup>) time?
 *
 * <h3>Solutions</h3>
 * <ul>
 *   <li>{@link TwoLoopSolution} — brute force, every pair; O(n<sup>2</sup>) time, O(1) space</li>
 *   <li>{@link HashMapSolution} — one pass with a map of needed complements; O(n) time, O(n) space</li>
 * </ul>
 *
 * <h3>Source</h3>
 * <p>LeetCode #1; practised for a Technical round, Oct 2026
 *
 * @author Ravi Kamble
 */
public interface TwoSumProblem {

    /**
     * Finds the indices of the two distinct elements whose sum equals {@code target}.
     *
     * <p>Implementations must <b>not</b> modify {@code nums}.
     *
     * @param nums   the input array
     * @param target the required sum
     * @return a two-element array holding the two (different) indices, in any order
     * @throws NullPointerException     if {@code nums} is {@code null}
     * @throws IllegalArgumentException if no two elements add up to {@code target}
     */
    int[] findIndices(int[] nums, int target);
}
