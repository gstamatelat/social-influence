package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.algorithms.generators.GraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.MutableGraph;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * A generator that produces directed path graphs.
 * <p>
 * The identification of this generator is
 * <ul>
 * <li>source: the vertex on the source tail of the path, which has no incoming edges</li>
 * <li>target: the vertex on the target tail of the path, which has no outgoing edges</li>
 * </ul>
 */
public class DirectedPathGenerator<V, E> implements GraphGenerator<DirectedGraph<V, E>, V, E> {
    private final int vertexCount;

    /**
     * Constructs a new {@link DirectedPathGenerator}.
     *
     * @param vertexCount the vertex count
     * @throws IllegalArgumentException if {@code vertexCount < 1}
     */
    public DirectedPathGenerator(int vertexCount) {
        if (vertexCount < 1) {
            throw new IllegalArgumentException();
        }
        this.vertexCount = vertexCount;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r,
                                        VertexProvider<V> vertexProvider,
                                        Map<String, V> identification) {
        final DirectedGraph<V, E> g = new MutableGraph<>(vertexCount);

        V current = g.addVertex(vertexProvider);
        identification.put("source", current);
        while (g.vertexCount() < vertexCount) {
            final V newVertex = g.addVertex(vertexProvider);
            g.addEdge(current, newVertex);
            current = newVertex;
        }
        identification.put("target", current);

        return g;
    }
}
