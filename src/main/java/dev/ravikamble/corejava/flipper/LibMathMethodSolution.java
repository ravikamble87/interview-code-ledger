package dev.ravikamble.corejava.flipper;

/**
 * <b>Approach — Library math method.</b>
 *
 * <p>The answer is {@code 1 - input}. {@link Math#subtractExact(int, int)}
 * does the subtraction inside the JDK, so the solution itself contains no
 * operator.
 *
 * <p><b>Invalid input:</b> fails silently for most values outside {0, 1}
 * ({@code flip(2) == -1}, {@code flip(-1) == 2}). Fails loudly only at
 * {@code Integer.MIN_VALUE}, where {@code 1 - MIN_VALUE} overflows and
 * {@code subtractExact} throws {@link ArithmeticException}.
 *
 * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1), no allocation
 *
 * @author Ravi Kamble
 */
public class LibMathMethodSolution implements BitFlipperProblem {

    @Override
    public int flip(int input) {
        return Math.subtractExact(1, input);
    }
}
