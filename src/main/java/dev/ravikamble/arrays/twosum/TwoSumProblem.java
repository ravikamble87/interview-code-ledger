package dev.ravikamble.arrays.twosum;

/**
 * <h2>Two Sum</h2>
 *
 * <p><b>Source:</b> LeetCode #1 · <b>Topic:</b> Arrays · <b>Difficulty:</b> Easy</p>
 *
 * <h3>Problem</h3>
 * <p>Given an array of integers {@code nums} and an integer {@code target}, return the
 * indices of the two numbers that add up to {@code target}.</p>
 * <p>Each input has exactly one solution, and you may not use the same element twice.
 * The indices may be returned in any order.</p>
 *
 * <h3>Examples</h3>
 * <pre>
 * Example 1:
 *   Input:  nums = [2, 7, 11, 15], target = 9
 *   Output: [0, 1]
 *   Explanation: nums[0] + nums[1] == 9
 *
 * Example 2:
 *   Input:  nums = [3, 2, 4], target = 6
 *   Output: [1, 2]
 *
 * Example 3:
 *   Input:  nums = [3, 3], target = 6
 *   Output: [0, 1]
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
 * <h3>Follow-up</h3>
 * <p>Can you come up with an algorithm that is better than O(n<sup>2</sup>) time?</p>
 */
public interface TwoSumProblem {

    /**
     * Finds the indices of the two distinct elements whose sum equals {@code target}.
     *
     * @param nums   the input array; not modified
     * @param target the required sum
     * @return a two-element array holding the two indices, in any order
     */
    int[] findIndices(int[] nums, int target);
}