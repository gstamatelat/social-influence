package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.GraphFactory;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * A generator that produces undirected cycle graphs.
 */
public class CycleGenerator implements GraphGenerator {
    private int vertexCount;

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
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory,
                                       Random r,
                                       VertexProvider<V> vertexProvider,
                                       Map<String, V> identification) {
        final Graph<V, E> g = factory.createGraph(vertexCount);

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

        return g;
    }
}
