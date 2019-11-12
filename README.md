# Three implementations of interval range merge

Written as an exercise for coding challenge.

## Implementations
- List-based merge: O(n*log n) complexity, O(n) memory
- Tree-based merge: O(n) complexity, O(n) memory
- Parallel tree-based merge: O(n) complexity, O(n) memory

Caveat: some consider big-O notation deceiving.

## How to run
This is a standard Maven project, use `mvn test`.

There's also a profile for code coverage; run `mvn test -Pcoverage`.
Results will be in target/site/jacoco/index.html.
