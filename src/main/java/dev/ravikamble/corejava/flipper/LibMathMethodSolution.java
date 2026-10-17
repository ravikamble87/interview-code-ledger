package dev.ravikamble.corejava.flipper;

public class LibMathMethodSolution implements BitFlipperProblem {
    @Override
    public int flip(int input) {
        return Math.subtractExact(1, input);
    }
}
