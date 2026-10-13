# Interview Code Ledger

> A ledger of real Java interview questions — each one solved, tested and explained.

Every problem in this repository was **actually asked in a technical interview**. Like a ledger, entries are recorded as they happen and are only ever added to: new problems are appended, and new solutions sit alongside existing ones rather than replacing them.

The goal is not just to show *an* answer, but to show **how a problem is approached the way it should be in production code** — with a clear contract, multiple approaches, documented trade-offs and automated tests.

---

## How This Repository Works

The repository follows one simple pattern for every problem:

| Piece | Role |
|---|---|
| **Interface** | The *problem*. Its Javadoc holds the problem statement, examples, edge cases, follow-up questions and the source (interview round). The method signature is the contract every solution must honour. |
| **Implementations** | The *solutions*. Each approach is a separate class implementing the problem interface, with its own Javadoc describing the approach, time/space complexity and any limitations. |
| **Test class** | The *judge*. One shared test suite runs **every registered solution** against the same test cases, so any new solution is automatically verified against everything the existing ones are. |

```
            ┌──────────────────────────┐
            │   OddEvenSorter          │  ← interface = problem statement + contract
            │   (problem #001)         │
            └────────────┬─────────────┘
                         │ implements
          ┌──────────────┴──────────────┐
          ▼                             ▼
 ┌──────────────────┐         ┌──────────────────┐
 │ PartitionSolution │         │ NegationSolution │  ← solutions = different approaches
 └──────────────────┘         └──────────────────┘
          ▲                             ▲
          └──────────────┬──────────────┘
                         │ runs every solution against the same cases
            ┌────────────┴─────────────┐
            │   OddEvenSorterTest      │  ← shared test suite
            └──────────────────────────┘
```

### Why this design

- **One contract, many solutions** — comparing approaches side by side is exactly what interviewers probe: *"Can you do it another way? What's the trade-off?"*
- **Tests are shared, not duplicated** — adding a solution means adding one class and one line in the test; it is immediately checked against all existing cases.
- **Limitations are explicit** — a solution that only works for a subset of inputs (e.g. non-negative numbers) is registered as such and tested only on what it claims to support.
- **Readable test output** — tests use `@ParameterizedTest` with named arguments, so results read like `PartitionSolution: [12, 1, 8…] → [9, 7, 5…]`.

---

## Problem Index

| # | Problem | Topic | Round | Difficulty | Solutions |
|---|---|---|---|---|---|
| 001 | [Sort Odds Descending, Evens Ascending](src/main/java/dev/ravikamble/arrays/oddevensorter/OddEvenSorter.java) | Arrays, Sorting | Technical L1 (Fintech) | Easy | `PartitionSolution`, `NegationSolution` |

> Company names are intentionally not listed. Questions are tagged by round and domain only.

---

## Project Structure

```
interview-code-ledger/
├── README.md
├── pom.xml
└── src/
    ├── main/java/dev/ravikamble/
    │   └── arrays/
    │       └── oddevensorter/
    │           ├── OddEvenSorter.java        ← problem (interface)
    │           ├── PartitionSolution.java    ← solution
    │           └── NegationSolution.java     ← solution
    └── test/java/dev/ravikamble/
        └── arrays/
            └── oddevensorter/
                └── OddEvenSorterTest.java    ← shared tests
```

Problems are grouped by topic package (`arrays`, `strings`, `collections`, `concurrency`, `streams`, …). As a topic grows, each problem moves into its own sub-package (e.g. `dev.ravikamble.arrays.oddevensorter`).

---

## Tech Stack

- **Java 21**
- **Maven**
- **JUnit 5** (Jupiter) — parameterized tests, `@MethodSource`, `Named` arguments

---

## Getting Started

**Prerequisites:** JDK 21 and Maven 3.9+

```bash
git clone https://github.com/ravikamble87/interview-code-ledger.git
cd interview-code-ledger
mvn test
```

To run the tests for a single problem:

```bash
mvn test -Dtest=OddEvenSorterTest
```

---

## Anatomy of a Problem

### 1. The interface — the problem

```java
/**
 * <h2>Problem #001 — Sort Odds Descending, Evens Ascending</h2>
 * ... problem statement, examples, edge cases, follow-up ...
 *
 * <h3>Solutions</h3>
 * <ul>
 *   <li>{@link PartitionSolution} — general; handles any integer</li>
 *   <li>{@link NegationSolution} — non-negative input only</li>
 * </ul>
 *
 * <h3>Source</h3>
 * <p>Asked in: Technical round (Fintech), Oct 2026
 */
public interface OddEvenSorter {
    int[] sort(int[] input);
}
```

### 2. The implementations — the solutions

```java
/**
 * Approach — Partition, then sort each range.
 * Time: O(n log n)   Space: O(n)
 */
public class PartitionSolution implements OddEvenSorter {
    @Override
    public int[] sort(int[] input) { ... }
}
```

### 3. The test — the judge

```java
/** Solutions that must handle ANY input. */
private static final List<OddEvenSorter> GENERAL_SOLUTIONS = List.of(
        new PartitionSolution()
);

/** Solutions documented as valid only for a subset of input. */
private static final List<OddEvenSorter> NON_NEGATIVE_ONLY_SOLUTIONS = List.of(
        new NegationSolution()
);
```

Each test suite typically includes:

- **Example-based cases** — the examples from the problem statement
- **Edge cases** — empty input, single element, duplicates, extremes (`Integer.MIN_VALUE` / `MAX_VALUE`)
- **Contract checks** — e.g. *the solution must not modify its input*
- **Randomised checks** — solutions are compared against a simple, obviously-correct reference implementation on many random inputs (fixed seed, so failures are reproducible)

---

## Adding a New Problem

1. Pick the topic package (or create one), e.g. `dev.ravikamble.strings`.
2. Create the **interface**. Its Javadoc must include:
   - Problem number and title
   - Problem statement
   - At least two examples (input → output)
   - Edge cases
   - Follow-up questions (if any were asked)
   - Source — interview round and domain (no company names)
3. Create at least one **solution class** implementing the interface, with a Javadoc covering the approach, time/space complexity and limitations.
4. Create the **test class** with the solution registered and test cases covering examples and edge cases.
5. Add a row to the **Problem Index** above.
6. Run `mvn test` — everything must pass.

---

## Contributing a Different Solution

Have a different way to solve an existing problem? Contributions are welcome.

1. Fork the repository and create a branch.
2. Add a **new class** implementing the problem's interface. Do not modify existing solutions.
3. Write a Javadoc explaining:
   - The approach
   - Time and space complexity
   - How it differs from the existing solutions
   - Any input limitations
   - `@author` with your name / GitHub handle
4. Register your class in the problem's test class:
   - in the general list if it handles all inputs, or
   - in the restricted list if it only supports a subset (and say so in the Javadoc).
5. Add your solution to the **Solutions** list in the interface Javadoc and to the Problem Index.
6. Run `mvn test` and open a pull request.

A solution is merged only if all tests pass.

---

## Conventions

- **Naming** — interface named after the problem (`OddEvenSorter`); solutions named after their approach (`PartitionSolution`, `NegationSolution`).
- **Inputs are not mutated** — unless a problem explicitly asks for an in-place algorithm, solutions return a new result and leave the caller's data untouched.
- **Primitives where it matters** — prefer `int[]` over `Integer[]` to avoid boxing overhead.
- **Honest limitations** — if a solution breaks for some inputs, the Javadoc says so and the tests reflect it.
- **No printing in solutions** — output is verified by tests, not by `System.out`.

---

## Author

**Ravi Kamble** — Technical Lead, Java / Distributed Systems / Capital Markets

- GitHub: [@ravikamble87](https://github.com/ravikamble87)
- LinkedIn: [ravikamble87](https://www.linkedin.com/in/ravikamble87)

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
