package dev.ravikamble.arrays.oddevensorter;

import java.util.Arrays;

/**
 * <b>Approach — Negation trick.</b>
 *
 * <p>Negate every odd number, sort once ascending, then negate the odds back.
 * Negated odds become negative, so they sort before the evens and in
 * reversed (descending) order, all in a single sort.
 *
 * <p><b>Limitation:</b> valid only for <b>non-negative</b> input. A negative odd
 * becomes positive after negation and mixes into the even range, and a
 * negative even sorts ahead of the odds.
 * Example: {@code [-4, 3]} returns {@code [-4, 3]} instead of {@code [3, -4]}.
 *
 * <p><b>Overflow:</b> safe. Odd values can never be {@code Integer.MIN_VALUE}
 * (which is even), so negating an odd never overflows.
 *
 * <p><b>Time:</b> O(n log n) &nbsp; <b>Space:</b> O(n) for the copy
 *
 * @author Ravi Kamble
 */
public class NegationSolution implements OddEvenSorter {

    @Override
    public int[] sort(int[] input) {
        int[] result = input.clone();

        negateOdds(result);
        Arrays.sort(result);
        negateOdds(result);

        return result;
    }

    private static void negateOdds(int[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] % 2 != 0) {
                values[i] = -values[i];
            }
        }
    }
}