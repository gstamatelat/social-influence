package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.GraphFactory;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * A generator that produces undirected path graphs.
 * <p>
 * The identification of this generator is
 * <ul>
 * <li>left: the vertex on one tail of the path</li>
 * <li>right: the vertex on the other tail of the path</li>
 * </ul>
 */
public class PathGenerator implements GraphGenerator {
    private final int vertexCount;

    /**
     * Constructs a new {@link PathGenerator}.
     *
     * @param vertexCount the vertex count
     * @throws IllegalArgumentException if {@code vertexCount < 1}
     */
    public PathGenerator(int vertexCount) {
        if (vertexCount < 1) {
            throw new IllegalArgumentException();
        }
        this.vertexCount = vertexCount;
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory,
                                       Random r,
                                       VertexProvider<V> vertexProvider,
                                       Map<String, V> identification) {
        final Graph<V, E> g = factory.createWeightedDirected(vertexCount);

        V current = g.addVertex(vertexProvider);
        identification.put("left", current);
        while (g.vertexCount() < vertexCount) {
            final V newVertex = g.addVertex(vertexProvider);
            g.addEdge(current, newVertex);
            g.addEdge(newVertex, current);
            current = newVertex;
        }
        identification.put("right", current);

        return g;
    }
}
