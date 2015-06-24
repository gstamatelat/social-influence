# Social Influence

Java network/graph framework with emphasis on social influence

## Complexity notation

Running time - Complexity:

* Very Fast - Constant (few cycles)
* Fast - Linear
* Slow - Polynomial (not linear - n^2)
* Very Slow - Exponential (use a different programming language)

> See: [Java Collections - Performance (Time Complexity)](http://infotechgems.blogspot.com/2011/11/java-collections-performance-time.html)

## TODO

- There needs to be a helper function or some `Graph` member method that can return if a graph is aperiodic or not.
- Consider converting `Move` to immutable.
- Considering converting `Move` to `Map`.
- `Player` should not be able to mutate the `Graph` object `g`. Consider passing a copy instead.
- Create a `MutableGraph` class that will derive `Graph`. 
- `getVertexFromId()` is confusing and needs to go.
- `Game` should accept `Player`s not `Move`s.
- Grid generator and maybe some edge randomizer: http://www.cs.cornell.edu/home/kleinber/swn.d/swn.html
- Method "stretch" that extends edges
- Restore the inputs Graph and GameDefinition on Player and create seperate class `PlayerExecutor` that executes it. `PlayerExecutor` should extend ThreadPool or sth and will be able to execute 2 players at once