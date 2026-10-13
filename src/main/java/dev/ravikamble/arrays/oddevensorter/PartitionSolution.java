package dev.ravikamble.arrays.oddevensorter;

import java.util.Arrays;

/**
 * <b>Approach — Partition, then sort each range.</b>
 *
 * <ol>
 *   <li>Partition in one pass: move all odds to the front, evens to the back
 *       (like the partition step of quicksort). The boundary index separates them.</li>
 *   <li>Sort the odd range ascending, then reverse it to get descending.</li>
 *   <li>Sort the even range ascending.</li>
 * </ol>
 *
 * <p>Works for <b>any</b> integers, including negatives, zero and
 * {@code Integer.MIN_VALUE}/{@code MAX_VALUE}. Uses primitive {@code int[]}
 * throughout, so there is no boxing.
 *
 * <p><b>Time:</b> O(n) partition + O(n log n) sorts = O(n log n)
 * &nbsp; <b>Space:</b> O(n) for the copy
 *
 * @author Ravi Kamble
 */
public class PartitionSolution implements OddEvenSorter {

    @Override
    public int[] sort(int[] input) {
        int[] result = input.clone();

        int boundary = partitionOddsFirst(result);

        Arrays.sort(result, 0, boundary);              // odds ascending
        reverse(result, 0, boundary);                  // odds descending
        Arrays.sort(result, boundary, result.length);  // evens ascending

        return result;
    }

    /**
     * Moves odd numbers to the front of the array.
     *
     * @return index of the first even number (= count of odd numbers)
     */
    private static int partitionOddsFirst(int[] values) {
        int nextOddSlot = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] % 2 != 0) {
                swap(values, i, nextOddSlot++);
            }
        }
        return nextOddSlot;
    }

    /** Reverses values in the range [from, to). */
    private static void reverse(int[] values, int from, int to) {
        for (int i = from, j = to - 1; i < j; i++, j--) {
            swap(values, i, j);
        }
    }

    private static void swap(int[] values, int i, int j) {
        int temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }
}