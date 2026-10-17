package dev.ravikamble.corejava.flipper;

/**
 * <pre>
 * ============================================================================
 * PROBLEM #004 — BIT FLIPPER WITHOUT OPERATORS
 * ============================================================================
 * Write a method that returns 0 when given 1, and returns 1 when given 0,
 * WITHOUT using any operator.
 *
 * Example:
 *   flip(1) -> 0
 *   flip(0) -> 1
 *
 * Not allowed (clarify the full list with the interviewer):
 *   - Arithmetic      : +  -  *  /  %
 *   - Bitwise         : ^  ~  &amp;  |  &lt;&lt;  &gt;&gt;
 *   - Logical         : !  &amp;&amp;  ||
 *   - Relational      : ==  !=  &lt;  &gt;
 *   - Ternary         : ?:
 *   - if / else / switch on the value (often disallowed in spirit)
 *
 * ============================================================================
 * SCENARIOS
 * ============================================================================
 * S1. Input 1 -> 0
 * S2. Input 0 -> 1
 * S3. Repeated calls -> flip(flip(x)) == x  (round trip returns original)
 *
 * ============================================================================
 * EDGE CASES
 * ============================================================================
 * E1. Input outside {0, 1}   -> 2, 5, 100: throw, return garbage, or undefined?
 * E2. Negative input         -> -1: what happens with your approach?
 * E3. Boundary values        -> Integer.MAX_VALUE, Integer.MIN_VALUE
 * E4. Boxed input            -> flip(Integer null) -> NPE on unboxing?
 * E5. Input type             -> int vs boolean vs char '0'/'1' vs String "0"/"1"
 *                               — does the approach change?
 *
 * ============================================================================
 * CLARIFYING QUESTIONS (ask before coding)
 * ============================================================================
 * Q1. Is '=' (assignment) counted? Is array indexing [] counted?
 * Q2. Are method calls / library methods (Math, Integer, etc.) allowed?
 * Q3. Is control flow (if / switch) allowed if no operator appears?
 * Q4. Is input guaranteed to be only 0 or 1?
 *
 * ============================================================================
 * FOLLOW-UP QUESTIONS (interviewer may ask)
 * ============================================================================
 * F1. How many distinct approaches can you come up with?
 * F2. Which approach fails loudly on invalid input, and which fails silently?
 * F3. Which is fastest / allocation-free?
 * F4. Generalize: given a and b, return b when passed a, and a when passed b.
 *
 * ============================================================================
 * SOLUTIONS
 * ============================================================================
 * LibMathMethodSolution    -> Math.subtractExact(1, input)
 * ComparisonMethodSolution -> Integer.compare(1, input)
 *
 * ============================================================================
 * SOURCE
 * ============================================================================
 * Asked in: Technical round, Oct 2026
 * </pre>
 *
 * @author Ravi Kamble
 */
public interface BitFlipperProblem {

    /**
     * Returns 1 when given 0, and 0 when given 1, without using any operator.
     *
     * @param input 0 or 1; behaviour for any other value is up to the solution
     * @return the flipped bit
     */
    int flip(int input);
}