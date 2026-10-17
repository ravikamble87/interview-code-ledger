package dev.ravikamble.corejava.flipper;

public class ComparisonMethodSolution implements BitFlipperProblem {
    @Override
    public int flip(int input) {
        return Integer.compare(1, input);
    }
}
