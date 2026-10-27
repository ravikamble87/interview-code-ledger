package dev.ravikamble.arrays.twosum;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Approach — One pass, remember the complement each element is waiting for.</b>
 *
 * <ol>
 *   <li>Walk the array once. For each {@code nums[i]}, first check whether an earlier element is
 *       waiting for exactly this value. If so, return that element's index and {@code i}.</li>
 *   <li>Otherwise record {@code target - nums[i] -> i}: "index {@code i} is waiting for this value".</li>
 * </ol>
 *
 * <p>The map stores <i>what is needed</i> rather than <i>what has been seen</i>; both variants are
 * equivalent. Checking before inserting means an element can never pair with itself, and
 * duplicates such as {@code [3, 3]} work naturally.
 *
 * <p><b>Overflow:</b> within the constraints {@code target - nums[i]} is at most 2 * 10<sup>9</sup>,
 * which fits in an {@code int}. Outside the constraints it can overflow.
 *
 * <p><b>Time:</b> O(n) expected (O(1) average map lookups) &nbsp; <b>Space:</b> O(n) for the map
 *
 * @author Ravi Kamble
 */
public class HashMapSolution implements TwoSumProblem {

    @Override
    public int[] findIndices(int[] nums, int target) {
        Map<Integer, Integer> waitingFor = new HashMap<>();   // needed value -> index that needs it

        for (int i = 0; i < nums.length; i++) {
            Integer partner = waitingFor.get(nums[i]);        // one lookup instead of containsKey + get
            if (partner != null) {
                return new int[]{partner, i};
            }
            waitingFor.put(target - nums[i], i);
        }
        throw new IllegalArgumentException("No two elements add up to " + target);
    }
}
