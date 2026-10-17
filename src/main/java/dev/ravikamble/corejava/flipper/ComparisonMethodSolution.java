package dev.ravikamble.corejava.flipper;

/**
 * <b>Approach — Comparison method.</b>
 *
 * <p>{@link Integer#compare(int, int)} returns 0 when both values are equal
 * and 1 when the first is greater. So {@code compare(1, 1) == 0} and
 * {@code compare(1, 0) == 1}, which is exactly the flip, with no operator.
 *
 * <p><b>Invalid input:</b> fails silently and never throws. Any value above 1
 * returns -1, and any value below 0 returns 1, which looks like a valid
 * answer even though the input was wrong.
 *
 * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1), no allocation
 *
 * @author Ravi Kamble
 */
public class ComparisonMethodSolution implements BitFlipperProblem {

    @Override
    public int flip(int input) {
        return Integer.compare(1, input);
    }
}
