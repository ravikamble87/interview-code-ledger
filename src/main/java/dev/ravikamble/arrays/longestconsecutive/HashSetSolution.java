package dev.ravikamble.arrays.longestconsecutive;

import java.util.HashSet;
import java.util.Set;

/**
 * <b>Approach — HashSet, start counting only at the beginning of a run.</b>
 *
 * <ol>
 *   <li>Put every value into a {@link HashSet}. Duplicates collapse for free.</li>
 *   <li>For each value {@code n} in the set, if {@code n - 1} is also in the set,
 *       {@code n} is in the middle of a run, so skip it.</li>
 *   <li>Otherwise {@code n} starts a run: count upwards while {@code n + 1},
 *       {@code n + 2}, … are present.</li>
 * </ol>
 *
 * <p>Each value is visited at most twice (once in the outer loop, once while
 * counting up its run), which gives O(n) expected time.
 *
 * <p><b>Overflow:</b> the {@code n - 1} check is skipped for
 * {@code Integer.MIN_VALUE}, and counting stops at {@code Integer.MAX_VALUE},
 * so a run never wraps round from MAX to MIN.
 *
 * <p><b>Time:</b> O(n) expected &nbsp; <b>Space:</b> O(n) for the set
 *
 * @author Ravi Kamble
 */
public class HashSetSolution implements LongestConsecutiveSequenceProblem {

    @Override
    public int longestConsecutive(int[] input) {
        Set<Integer> values = new HashSet<>();
        for (int n : input) {
            values.add(n);
        }

        int longest = 0;
        for (int start : values) {
            if (start != Integer.MIN_VALUE && values.contains(start - 1)) {
                continue;   // not the start of a run
            }

            int length = 1;
            int current = start;
            while (current != Integer.MAX_VALUE && values.contains(current + 1)) {
                current++;
                length++;
            }
            longest = Math.max(longest, length);
        }
        return longest;
    }
}
