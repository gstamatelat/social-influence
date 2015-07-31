# Social Influence [![Build Status](https://travis-ci.org/gstamatelat/social-influence.svg?branch=master)](https://travis-ci.org/gstamatelat/social-influence) [![Dependency Status](https://www.versioneye.com/user/projects/5596a989616634001b000007/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5596a989616634001b000007)

Java network/graph framework with emphasis on social influence

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
GridGenerator generator = new GridGenerator<>(MemoryGraph.class, 25, 35);
Graph gridGraph = generator.create();
System.out.println(gridGraph);
```

Inspect available generators in `gr.james.socialinfluence.algorithms.generators` package.

### Vertex iteration

```java
Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
for (Vertex v : g.getVertices()) {
    // Do something with v
}
```

The order at which the default iterator traverses vertices depends on the graph implementation. You should use this construct if you don't care about the iteration order. If you do, consider using a specific iterator. Iterators (unless stated) are not backed by the graph; changes on the graph structure while an iteration is in progress won't reflect on the iterators.

`RandomVertexIterator` iterates over vertices in a random order.

```java
Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
RandomVertexIterator vi = new RandomVertexIterator(g);
while (vi.hasNext()) {
    Vertex v = vi.next();
    // Do something with v
}
```

`IndexVertexIterator` iterates over vertices based on their index.

```java
Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
IndexVertexIterator vi = new IndexVertexIterator(g);
while (vi.hasNext()) {
    Vertex v = vi.next();
    // Do something with v
}
```

## TODO

- There needs to be a helper function or some `Graph` member method that can return if a graph is aperiodic or not.
- Consider converting `Move` to immutable.
- Considering converting `Move` to `Map`.
- Grid generator and maybe some edge randomizer: http://www.cs.cornell.edu/home/kleinber/swn.d/swn.html
- Transformation "stretch" that extends edges
- Perhaps define an `interface VertexSimilarity` with one member `double compute(Vertex v1, Vertex v2, Graph g)` as well as an `interface VertexSimilarityMatrix` replacing `Map<VertexPair, Double>`. See '3.2.4. Definitions based on vertex similarity' in 'Community detection in graphs, Santo Fortunato'
- A PageRank test on a known graph
- GraphOperations.combineGraphs seems like a generalization of Graph.deepCopy
