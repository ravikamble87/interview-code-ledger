package dev.ravikamble.collectionsandstreams.fruitvegseparator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Optimization: split each string once, up front, in map(), before grouping.
 *
 * If the split happened inside groupingBy instead (once in the key classifier
 * for the type, and again in the downstream mapper for the name), every string
 * would be split twice. Splitting first turns each string into a String[] a
 * single time, and both the key and the value read from that same array.
 *
 * On large production inputs this avoids double the parsing work and the
 * extra temporary array created by each split.
 */
public class SpitAndGroupBySolution implements FruitVegSeparatorProblem {
    @Override
    public Map<String, List<String>> separate(List<String> items) {
        return items.stream()
                .map(s -> s.split(":", 2))
                .collect(Collectors.groupingBy(parts -> parts[0],
                        Collectors.mapping(parts -> parts[1], Collectors.toList())));
    }

    public static void main(String[] args) {
        List<List<String>> problem = new ArrayList<>();
        FruitVegSeparatorProblem separator = new SpitAndGroupBySolution();


        // S1: Happy path, mixed list
        // -> {Fruit=[Apple, Banana], Vegetable=[Potato, Onion]}
        List<String> mixed = List.of("Fruit:Apple", "Vegetable:Potato", "Fruit:Banana", "Vegetable:Onion");
        problem.add(mixed);

        // S2: Only one type present
        // -> {Fruit=[Apple, Mango]}  (no Vegetable key)
        List<String> onlyFruits = List.of("Fruit:Apple", "Fruit:Mango");
        problem.add(onlyFruits);

        // S6: Duplicates
        // -> {Fruit=[Apple, Apple], Vegetable=[Potato]}
        List<String> duplicates = List.of("Fruit:Apple", "Fruit:Apple", "Vegetable:Potato");
        problem.add(duplicates);

        // E2: Empty list
        // -> {}
        List<String> empty = List.of();
        problem.add(empty);

        // E6, E7, E8: Format quirks
        // -> {=[Apple], Fruit=[, Apple:Red]}
        List<String> formatQuirks = List.of("Fruit:", ":Apple", "Fruit:Apple:Red");
        problem.add(formatQuirks);

        // E9, E10, E11: Whitespace, case and spelling variants
        // -> 5 separate keys: " Fruit ", "fruit", "FRUIT", "Vegitable", "Vegetable"
        List<String> variants = List.of(" Fruit : Apple ", "fruit:Mango", "FRUIT:Grapes", "Vegitable:Potato", "Vegetable:Onion");
        problem.add(variants);

        // E12: Unknown type
        // -> {Fruit=[Apple], Nut=[Almond]}
        List<String> unknownType = List.of("Fruit:Apple", "Nut:Almond");
        problem.add(unknownType);

        // E5: No delimiter
        // -> ArrayIndexOutOfBoundsException
        List<String> noDelimiter = List.of("Fruit:Apple", "Potato");
        problem.add(noDelimiter);

        // E3: Null element (List.of rejects nulls, so use Arrays.asList)
        // -> NullPointerException
        List<String> withNull = Arrays.asList("Fruit:Apple", null);
        problem.add(withNull);

        // E4: Blank string
        // -> ArrayIndexOutOfBoundsException
        List<String> blank = List.of("Fruit:Apple", "  ");
        problem.add(blank);

        // E1: Null list
        // -> NullPointerException
        List<String> nullList = null;
        problem.add(nullList);

        for (List<String> input : problem) {
            try {
                System.out.println(input + " -> " + separator.separate(input));
            } catch (Exception e) {
                System.out.println(input + " -> " + e.getClass().getSimpleName());
            }
        }
    }
}
