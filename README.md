# Social Influence

[![Build Status](https://travis-ci.org/gstamatelat/social-influence.svg?branch=master)](https://travis-ci.org/gstamatelat/social-influence) [![Dependency Status](https://www.versioneye.com/user/projects/55d2318a265ff6001c00001d/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d2318a265ff6001c00001d)

Java 8 network/graph framework with emphasis on social influence and opinion dynamics.

## Using

Add the following to your build.gradle file:

```gradle
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    compile 'com.github.gstamatelat:social-influence:SHORT-COMMIT-HASH'
}
```

for example `compile 'com.github.gstamatelat:social-influence:fc8f5d39fe'`

## Graph creation

```java
Graph g = new MemoryGraph();
Vertex v1 = g.addVertex();
Vertex v2 = g.addVertex();
g.addEdge(v1, v2);
```

## Graph generation

All generators implement the `GraphGenerator` interface. A primitive example that generates a random graph with *100* vertices and connection probability *0.15* is given below:

```java
GraphGenerator generator = new RandomGenerator(100, 0.15);
Graph randomGraph = generator.generate();
System.out.println(randomGraph);
```

Each call of `generate()` or any other form of this method will always allocate and return a new graph.

### Non-deterministic generators

Some graph models (including `RandomGenerator`) are non-deterministic; successive calls to `generate()` may produce different graphs. Eliminate randomness by using the following forms:

- `generate(long)` will attach a seed to the generation, thus always producing identical copies of the same graph with a set seed.
- `generate(Random)` will force the generation to use a specific `Random` object. The default is to use a framework-wide `Random` instance.

These forms do not differentiate the behavior of generation on deterministic graphs, for example on `WheelGenerator`.

### Force a specific graph type

The type (also referred to as container) of `Graph` that the generator will produce can be specified using the overloaded forms of `generate` with a `GraphFactory<T>`, which will produce a graph of type `T`. If this is not specified, the default type of `MemoryGraph` will be used. In the table below, the correspondences between the methods with and without `GraphFactory<T>` are presented.

Default type       | Specified type
------------------ | --------------
`generate()`       | `generate(GraphFactory<T>)`
`generate(long)`   | `generate(GraphFactory<T>, long)`
`generate(Random)` | `generate(GraphFactory<T>, Random)`

`GraphFactory<T>` is a functional interface containing only the method `create()`, which has to return a newly allocated empty graph of type `T`.

```java
GraphGenerator generator = new RandomGenerator(100, 0.15);
Graph randomGraph = generator.generate(MemoryGraph::new);
System.out.println(randomGraph);
```

The above snippet will generate a random graph with *100* vertices and connection probability *0.15* and return it as a `MemoryGraph` using a lambda implementation of `GraphFactory<T>` (although this is the default behavior, it is more explicit).

### Graph decorator

Sometimes it is useful to have a generator always return a predefined graph.

```java
Graph g = new RandomGenerator(100, 0.15).generate();
GraphGenerator gen = GraphGenerator.decorate(g);
```

After this, calls to `gen.generate()` will always return a copy of `g`. This decorator respects the overloaded forms with `GraphFactory<T>` but specifying a seed or a `Random` instance is unnecessary. Common usage cases of the decorator include:

0. Wrapping a graph that is imported or read from a stream to avoid repetitive I/O.
0. Wrapping a deterministic graph to avoid generation-related computational costs.

In both cases, however, the cost of copying is introduced, since calling `generate()` on a decorated `Graph` must allocate a new graph and copy the original.

### Available generators

Inspect available generators in `gr.james.influence.algorithms.generators` package.

## Vertex iteration

```java
Graph g = new RandomGenerator(100, 0.05).generate();
for (Vertex v : g) {
    // Do something with v
}
```

The above construct will traverse the graph vertices in the order they were inserted in the graph. If you need to perform an index-based iteration, you should use

```java
Graph g = new RandomGenerator(100, 0.05).generate();
for (int i = 0; i < g.getVerticesCount(); i++) {
    Vertex v = g.getVertexFromIndex(i);
    // Do something with v
}
```

**Index** is a deterministic, per-graph attribute between *0* (inclusive) and *N* (exclusive), where *N* the number of vertices in the graph, indicating the order at which the vertices were inserted in the graph.

Iterators (unless stated) are generally not backed by the graph; changes on the graph structure while an iteration is in progress won't reflect on the iterators.

`RandomVertexIterator` iterates over vertices in a random order.

```java
Graph g = new RandomGenerator(100, 0.05).generate();
RandomVertexIterator vi = new RandomVertexIterator(g);
while (vi.hasNext()) {
    Vertex v = vi.next();
    // Do something with v
}
```

## Graph implementations

                | MemoryGraph
--------------- | -----------
Storage         | O(n+m)
Add vertex      | O(1)
Add edge        | O(1)
Remove vertex   | O(n+m)
Remove edge     | O(1)
Contains vertex | O(1)
Contains edge   | O(1)

`MemoryGraph` is implemented using in-memory adjacency lists and is suitable for sparse graphs. It is the default graph implementation; references to the "default graph type", "default graph" or similar point to `MemoryGraph`.

### Unmodifiable graphs

`ImmutableGraph` is a graph decorator that implements the `Graph` interface and represents an unmodifiable (read-only) graph. Attempting to modify an `ImmutableGraph` will result in an `UnsupportedOperationException`. You can only create an `ImmutableGraph` using the static `decorate(Graph)` method. This method doesn't have any computational cost associated with it.

```java
Graph g = new RandomGenerator(100, 0.05).generate();
Graph ig = ImmutableGraph.decorate(g);
ig.addVertex(); // Will throw UnsupportedOperationException
```

Modifications that trigger exceptions include vertex addition/removal, edge addition/removal or weight modification as well as altering metadata information. `ImmutableGraph` is backed by the graph it decorates; changes to the underlying graph will immediately reflect on the `ImmutableGraph`.

# TODO

- http://www.cs.cornell.edu/home/kleinber/swn.d/swn.html
- Transformation "stretch" that extends edges
- Perhaps define an `interface VertexSimilarity` with one member `double compute(Vertex v1, Vertex v2, Graph g)` as well as an `interface VertexSimilarityMatrix` replacing `Map<VertexPair, Double>`. See '3.2.4. Definitions based on vertex similarity' in 'Community detection in graphs, Santo Fortunato'
- PageRank and HITS test on a known graph
- GraphOperations.combineGraphs seems like a generalization of Graph.deepCopy
- Implement `Metadata` on `Vertex` and `Edge`
- Edge weight should be part of Graph and Edge should be arbitrary object
- Add a field or method or something on GraphGenerator to mark if it is deterministic or not. This may help in the future to cache deterministic graphs.
