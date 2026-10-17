package dev.ravikamble.arrays.longestconsecutive;

import java.util.Arrays;

/**
 * <b>Approach — Sort, then scan once.</b>
 *
 * <ol>
 *   <li>Copy the input and sort the copy, so consecutive values sit next to each other.</li>
 *   <li>Walk the sorted copy, comparing each value with the one before it:
 *     <ul>
 *       <li>same value → duplicate, skip it (the run neither grows nor breaks)</li>
 *       <li>exactly one more → the run grows</li>
 *       <li>anything else → a new run starts at length 1</li>
 *     </ul>
 *   </li>
 * </ol>
 *
 * <p>The "exactly one more" check is done in {@code long}, so
 * {@code Integer.MAX_VALUE + 1} cannot wrap round to {@code Integer.MIN_VALUE}.
 *
 * <p><b>Time:</b> O(n log n) for the sort &nbsp; <b>Space:</b> O(n) for the copy
 *
 * @author Ravi Kamble
 */
public class SortingSolution implements LongestConsecutiveSequenceProblem {

    @Override
    public int longestConsecutive(int[] input) {
        if (input.length == 0) {
            return 0;
        }

        int[] sorted = input.clone();
        Arrays.sort(sorted);

        int longest = 1;
        int current = 1;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] == sorted[i - 1]) {
                continue;
            }
            if ((long) sorted[i] == (long) sorted[i - 1] + 1) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }
        return longest;
    }
}
