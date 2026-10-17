package dev.ravikamble.collectionsandstreams.fruitvegseparator;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ============================================================================
 * PROBLEM #003 — FRUIT / VEGETABLE SEPARATOR
 * ============================================================================
 * Given a list of produce entries as "Type:Name" strings, separate them into
 * a Map keyed by type (Fruit / Vegetable) with the list of names as values,
 * using the Java Stream API.
 *
 * Constraint: the separation must be done in a SINGLE LINE (one stream
 * pipeline, no loops, no if/else, no intermediate collections).
 *
 * Example:
 *   Input : ["Fruit:Apple", "Vegetable:Potato", "Fruit:Banana", "Vegetable:Onion"]
 *   Output: { Fruit=[Apple, Banana], Vegetable=[Potato, Onion] }
 *
 * Note: the key is the part BEFORE ':' and the value is the part AFTER ':'.
 *
 * ============================================================================
 * SCENARIOS
 * ============================================================================
 * S1. Mixed list            -&gt; both keys present, names in correct buckets.
 * S2. Only fruits           -&gt; "Fruit" key only; or should "Vegetable" exist
 *                              with an empty list?
 * S3. Only vegetables       -&gt; mirror of S2.
 * S4. Single entry          -&gt; ["Fruit:Apple"] -&gt; { Fruit=[Apple] }
 * S5. Encounter order       -&gt; names keep input order within each bucket.
 * S6. Duplicates            -&gt; ["Fruit:Apple", "Fruit:Apple"] -&gt; keep both
 *                              (List) or dedupe (Set)?
 * S7. Large input (1M+)     -&gt; single pass, O(n); split each string once.
 *
 * ============================================================================
 * EDGE CASES — INPUT LIST
 * ============================================================================
 * E1. Input list is null              -&gt; return empty map, or throw NPE?
 * E2. Input list is empty             -&gt; return empty map.
 * E3. Null element in the list        -&gt; skip, or fail fast?
 * E4. Empty / blank string ("", "  ") -&gt; skip, or fail fast?
 *
 * ============================================================================
 * EDGE CASES — "Type:Name" FORMAT
 * ============================================================================
 * E5.  No delimiter        -&gt; "Apple"              -&gt; split gives 1 part;
 *                                                     arr[1] -&gt; AIOOBE. Skip?
 * E6.  Missing name        -&gt; "Fruit:"             -&gt; split(":") drops the
 *                                                     trailing empty part!
 * E7.  Missing type        -&gt; ":Apple"             -&gt; key becomes "".
 * E8.  Extra delimiters    -&gt; "Fruit:Apple:Red"    -&gt; use split(":", 2) so
 *                                                     name = "Apple:Red"?
 * E9.  Whitespace          -&gt; " Fruit : Apple "    -&gt; trim both parts.
 * E10. Case variants       -&gt; "fruit:Apple", "FRUIT:Mango" -&gt; same key?
 *                                                     Normalize the type.
 * E11. Spelling variants   -&gt; "Vegitable" vs "Vegetable" -&gt; separate keys
 *                                                     unless normalized/mapped.
 * E12. Unknown type        -&gt; "Nut:Almond"         -&gt; new key, ignore, or
 *                                                     reject?
 * E13. Different delimiter -&gt; "Fruit-Apple", "Fruit=Apple" -&gt; invalid?
 *
 * ============================================================================
 * EDGE CASES — OUTPUT
 * ============================================================================
 * E14. Must NOT mutate the input list.
 * E15. Should the returned map be immutable?
 * E16. Parallel stream -&gt; still correct? order preserved per bucket?
 *
 * ============================================================================
 * FOLLOW-UP QUESTIONS (interviewer may ask)
 * ============================================================================
 * F1. groupingBy vs partitioningBy — which fits when keys are strings?
 *     What if the requirement is strictly "Fruit vs not Fruit"?
 * F2. Return counts per type -&gt; Map&lt;String, Long&gt;.
 * F3. Return sorted names per type, or a TreeMap of types.
 * F4. Parse into a record Produce(Category, String) first, then group by enum.
 * F5. Input as one String: "Fruit:Apple,Vegetable:Potato" — still one line?
 *
 * ============================================================================
 * SOLUTIONS
 * ============================================================================
 * SplitAndGroupBySolution -> split once in map(), then groupingBy + mapping
 *
 * ============================================================================
 * SOURCE
 * ============================================================================
 * Asked in: Technical round, Oct 2026
 * </pre>
 *
 * @author Ravi Kamble
 */
public interface FruitVegSeparatorProblem {

    /**
     * Groups {@code "Type:Name"} entries into a map of type to names.
     *
     * <p>Implementations must <b>not</b> modify {@code items}, and must keep
     * names in input order within each type.
     *
     * @param items entries in {@code "Type:Name"} form; may be empty
     * @return map of type to the names of that type, in input order
     */
    Map<String, List<String>> separate(List<String> items);
}
