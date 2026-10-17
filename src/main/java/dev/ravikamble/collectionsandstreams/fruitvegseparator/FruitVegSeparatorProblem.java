package dev.ravikamble.collectionsandstreams.fruitvegseparator;

import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * PROBLEM STATEMENT
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
 * S1. Mixed list            -> both keys present, names in correct buckets.
 * S2. Only fruits           -> "Fruit" key only; or should "Vegetable" exist
 *                              with an empty list?
 * S3. Only vegetables       -> mirror of S2.
 * S4. Single entry          -> ["Fruit:Apple"] -> { Fruit=[Apple] }
 * S5. Encounter order       -> names keep input order within each bucket.
 * S6. Duplicates            -> ["Fruit:Apple", "Fruit:Apple"] -> keep both
 *                              (List) or dedupe (Set)?
 * S7. Large input (1M+)     -> single pass, O(n); split each string once.
 *
 * ============================================================================
 * EDGE CASES — INPUT LIST
 * ============================================================================
 * E1. Input list is null              -> return empty map, or throw NPE?
 * E2. Input list is empty             -> return empty map.
 * E3. Null element in the list        -> skip, or fail fast?
 * E4. Empty / blank string ("", "  ") -> skip, or fail fast?
 *
 * ============================================================================
 * EDGE CASES — "Type:Name" FORMAT
 * ============================================================================
 * E5.  No delimiter        -> "Apple"              -> split gives 1 part;
 *                                                     arr[1] -> AIOOBE. Skip?
 * E6.  Missing name        -> "Fruit:"             -> split(":") drops the
 *                                                     trailing empty part!
 * E7.  Missing type        -> ":Apple"             -> key becomes "".
 * E8.  Extra delimiters    -> "Fruit:Apple:Red"    -> use split(":", 2) so
 *                                                     name = "Apple:Red"?
 * E9.  Whitespace          -> " Fruit : Apple "    -> trim both parts.
 * E10. Case variants       -> "fruit:Apple", "FRUIT:Mango" -> same key?
 *                                                     Normalize the type.
 * E11. Spelling variants   -> "Vegitable" vs "Vegetable" -> separate keys
 *                                                     unless normalized/mapped.
 * E12. Unknown type        -> "Nut:Almond"         -> new key, ignore, or
 *                                                     reject?
 * E13. Different delimiter -> "Fruit-Apple", "Fruit=Apple" -> invalid?
 *
 * ============================================================================
 * EDGE CASES — OUTPUT
 * ============================================================================
 * E14. Must NOT mutate the input list.
 * E15. Should the returned map be immutable?
 * E16. Parallel stream -> still correct? order preserved per bucket?
 *
 * ============================================================================
 * FOLLOW-UP QUESTIONS (interviewer may ask)
 * ============================================================================
 * F1. groupingBy vs partitioningBy — which fits when keys are strings?
 *     What if the requirement is strictly "Fruit vs not Fruit"?
 * F2. Return counts per type -> Map<String, Long>.
 * F3. Return sorted names per type, or a TreeMap of types.
 * F4. Parse into a record Produce(Category, String) first, then group by enum.
 * F5. Input as one String: "Fruit:Apple,Vegetable:Potato" — still one line?
 */
public interface FruitVegSeparatorProblem {

    Map<String, List<String>> separate(List<String> items);
}
