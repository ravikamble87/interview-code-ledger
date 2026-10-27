package dev.ravikamble.collectionsandstreams.top2cities;

/**
 * One payment transaction, as given in the problem.
 *
 * <p>Any field may be {@code null} in real data; see {@link TopCitiesProblem} for how such rows are
 * handled. {@code amount} is a {@code Double} to match the problem as asked; production code holding
 * money would use {@link java.math.BigDecimal}.
 *
 * @param id     transaction id, e.g. "T01"
 * @param amount transaction amount; may be {@code null}
 * @param status processing status, e.g. "SUCCESS", "FAILED", "PENDING"; may be {@code null}
 * @param city   city where the transaction happened; may be {@code null}
 *
 * @author Ravi Kamble
 */
public record Transaction(
        String id,
        Double amount,
        String status,
        String city) {
}
