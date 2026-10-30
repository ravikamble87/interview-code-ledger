package dev.ravikamble.collectionsandstreams.top2cities;

import java.util.*;
import java.util.stream.Collectors;

public class Solution2 implements TopCitiesProblem {
    @Override
    public Map<String, Double> topCities(List<Transaction> transactions) {
        return (Map<String, Double>) transactions.stream()
                .filter(t -> null != t && null != t.city() && null != t.amount() && null != t.status() && "SUCCESS".equalsIgnoreCase(t.status().trim()))
                .collect(Collectors.groupingBy(t->t.city().trim(),()->new TreeMap<>(String.CASE_INSENSITIVE_ORDER), Collectors.summingDouble(Transaction::amount)))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.<String, Double>comparingByValue())).limit(TOP_N)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }
}
