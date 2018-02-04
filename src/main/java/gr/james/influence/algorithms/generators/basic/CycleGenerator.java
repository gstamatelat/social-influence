package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * A generator that produces undirected cycle graphs.
 */
public class CycleGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    private final int vertexCount;

    /**
     * Construct a new {@link CycleGenerator}.
     *
     * @param vertexCount the vertex count
     * @throws IllegalArgumentException if {@code vertexCount < 3}
     */
    public CycleGenerator(int vertexCount) {
        if (vertexCount < 3) {
            throw new IllegalArgumentException();
        }
        this.vertexCount = vertexCount;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r,
                                        VertexProvider<V> vertexProvider,
                                        Map<String, V> identification) {
        final DirectedGraph<V, E> g = DirectedGraph.create(vertexCount);

        V current = g.addVertex(vertexProvider);
        final V start = current;
        while (g.vertexCount() < vertexCount) {
            final V newVertex = g.addVertex(vertexProvider);
            g.addEdge(current, newVertex);
            g.addEdge(newVertex, current);
            current = newVertex;
        }
        g.addEdge(current, start);
        g.addEdge(start, current);

        assert g.vertexCount() == vertexCount;

        return g;
    }
}
