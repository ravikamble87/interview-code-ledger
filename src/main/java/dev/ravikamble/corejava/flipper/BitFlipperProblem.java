package dev.ravikamble.corejava.flipper;

/**
 * <pre>
 * ============================================================================
 * PROBLEM STATEMENT
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
 * </pre>
 */
public interface BitFlipperProblem {
    int flip(int input);
}