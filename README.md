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

Inspect available generators in `gr.james.socialinfluence.graph.generators` package.

## TODO

- There needs to be a helper function or some `Graph` member method that can return if a graph is aperiodic or not.
- Consider converting `Move` to immutable.
- Considering converting `Move` to `Map`.
- Grid generator and maybe some edge randomizer: http://www.cs.cornell.edu/home/kleinber/swn.d/swn.html
- Transformation "stretch" that extends edges
