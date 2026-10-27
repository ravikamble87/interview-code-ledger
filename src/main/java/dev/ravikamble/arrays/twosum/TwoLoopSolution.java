package dev.ravikamble.arrays.twosum;

/**
 * <b>Approach — Brute force, check every pair.</b>
 *
 * <ol>
 *   <li>For each index {@code i}, try every later index {@code j > i}.</li>
 *   <li>Return {@code [i, j]} as soon as {@code nums[i] + nums[j] == target}.</li>
 * </ol>
 *
 * <p>Starting {@code j} at {@code i + 1} means an element is never paired with itself, and each
 * pair is checked only once: n(n-1)/2 checks in the worst case.
 *
 * <p><b>Overflow:</b> within the constraints the sum is at most 2 * 10<sup>9</sup>, which fits in
 * an {@code int}. For values outside the constraints, {@code nums[i] + nums[j]} can overflow.
 *
 * <p><b>Time:</b> O(n<sup>2</sup>) &nbsp; <b>Space:</b> O(1)
 *
 * @author Ravi Kamble
 */
public class TwoLoopSolution implements TwoSumProblem {

    @Override
    public int[] findIndices(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};   // return as soon as the pair is found
                }
            }
        }
        throw new IllegalArgumentException("No two elements add up to " + target);
    }
}
