# Social Influence

[![Build Status](https://travis-ci.org/gstamatelat/social-influence.svg?branch=master)](https://travis-ci.org/gstamatelat/social-influence) [![Dependency Status](https://www.versioneye.com/user/projects/55d2318a265ff6001c00001d/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d2318a265ff6001c00001d)

Java 8 network/graph framework with emphasis on social influence and opinion dynamics.

## Usage

### Graph creation

```java
Graph g = new MemoryGraph();
Vertex v1 = g.addVertex();
Vertex v2 = g.addVertex();
g.addEdge(v1, v2);
```

### Graph generation

```java
GraphGenerator generator = new RandomGenerator<>(MemoryGraph.class, 100, 0.05);
Graph randomGraph = generator.create();
System.out.println(randomGraph);
```

```java
GraphGenerator generator = new BarabasiAlbertGenerator<>(MemoryGraph.class, 25, 2, 2, 1.0);
Graph scaleFreeGraph = generator.create();
System.out.println(scaleFreeGraph);
```

```java
GraphGenerator generator = new GridGenerator<>(MemoryGraph.class, 25, 35);
Graph gridGraph = generator.create();
System.out.println(gridGraph);
```

```java
GraphGenerator generator = new WattsStrogatzGenerator<>(MemoryGraph.class, 1000, 6, 0.4);
Graph wattsStrogatzGraph = generator.create();
System.out.println(wattsStrogatzGraph);
```

Inspect available generators in `gr.james.socialinfluence.algorithms.generators` package.

### Vertex iteration

```java
Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
for (Vertex v : g) {
    // Do something with v
}
```

The above construct will traverse the graph vertices in the order they were inserted in the graph. If you need to perform an index-based iteration, you should use

```java
Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
for (int i = 0; i < g.getVerticesCount(); i++) {
    Vertex v = g.getVertexFromIndex(i);
    // Do something with v
}
```

**Index** is a deterministic, per-graph attribute between *0* (inclusive) and *N* (exclusive), where *N* the number of vertices in the graph, indicating the order at which the vertices were inserted in the graph.

Iterators (unless stated) are generally not backed by the graph; changes on the graph structure while an iteration is in progress won't reflect on the iterators.

`RandomVertexIterator` iterates over vertices in a random order.

```java
Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
RandomVertexIterator vi = new RandomVertexIterator(g);
while (vi.hasNext()) {
    Vertex v = vi.next();
    // Do something with v
}
```

### Graph implementations

                | MemoryGraph
--------------- | -----------
Storage         | O(n+m)
Add vertex      | O(1)
Add edge        | O(1)
Remove vertex   | O(m)
Remove edge     | O(1)
Contains vertex | O(1)
Contains edge   | O(1)

## TODO

- There needs to be a helper function or some `Graph` member method that can return if a graph is aperiodic or not
- Consider converting `Move` to immutable
- Grid generator and maybe some edge randomizer: http://www.cs.cornell.edu/home/kleinber/swn.d/swn.html
- Transformation "stretch" that extends edges
- Perhaps define an `interface VertexSimilarity` with one member `double compute(Vertex v1, Vertex v2, Graph g)` as well as an `interface VertexSimilarityMatrix` replacing `Map<VertexPair, Double>`. See '3.2.4. Definitions based on vertex similarity' in 'Community detection in graphs, Santo Fortunato'
- A PageRank test on a known graph
- GraphOperations.combineGraphs seems like a generalization of Graph.deepCopy
- Consider changing the iterator of Move to Iterator<MovePoint> or Iterator<GenericPair<Vertex,Double>>
- Implement `Metadata` on `Vertex` and `Edge`
