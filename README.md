# Social Influence

Java network/graph framework with emphasis on social influence

## Complexity notation

Running time - Complexity:

* Very Fast - Constant (few cycles)
* Fast - Linear
* Slow - Polynomial (not linear - n^2)
* Very Slow - Exponential (use a different programming language)

> See: [Java Collections – Performance (Time Complexity)](http://infotechgems.blogspot.com/2011/11/java-collections-performance-time.html)

## TODO

- There needs to be a helper function or some `Graph` member method that can return if a graph is aperiodic or not.
- Consider converting `Move` to immutable.
- Implement a method `void submitMove(Move m)` on `Player` class, which will replace the `MovePointer` concept. `submitMove` should deep copy the move before setting. Consider implementing a `recallMove()` as well for getting the last submitted move.