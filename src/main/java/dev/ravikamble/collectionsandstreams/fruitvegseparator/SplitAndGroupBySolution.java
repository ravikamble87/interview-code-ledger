package dev.ravikamble.collectionsandstreams.fruitvegseparator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <b>Approach — Split once, then groupingBy.</b>
 *
 * <p>Each {@code "Type:Name"} string is split into a {@code String[]} in
 * {@code map()}, then {@code groupingBy} uses part 0 as the key and a
 * {@code mapping} downstream collector keeps part 1 as the value.
 *
 * <p><b>Optimization:</b> split each string once, up front, before grouping.
 * If the split happened inside {@code groupingBy} instead (once in the key
 * classifier for the type, and again in the downstream mapper for the name),
 * every string would be split twice. Splitting first turns each string into a
 * {@code String[]} a single time, and both the key and the value read from that
 * same array. On large production inputs this avoids double the parsing work
 * and the extra temporary array created by each split.
 *
 * <p>{@code split(":", 2)} keeps an empty name ({@code "Fruit:"} gives
 * {@code ["Fruit", ""]}) and keeps extra colons inside the name
 * ({@code "Fruit:Apple:Red"} gives {@code ["Fruit", "Apple:Red"]}).
 *
 * <p><b>Limitations:</b> no trimming or case normalization, so
 * {@code " Fruit "}, {@code "fruit"} and {@code "Fruit"} are three different
 * keys. Fails fast on a null list or null element ({@link NullPointerException})
 * and on an entry with no {@code ':'} ({@link ArrayIndexOutOfBoundsException}).
 *
 * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(n) for the result map
 *
 * @author Ravi Kamble
 */
public class SplitAndGroupBySolution implements FruitVegSeparatorProblem {

    @Override
    public Map<String, List<String>> separate(List<String> items) {
        return items.stream()
                .map(s -> s.split(":", 2))
                .collect(Collectors.groupingBy(parts -> parts[0],
                        Collectors.mapping(parts -> parts[1], Collectors.toList())));
    }
}
