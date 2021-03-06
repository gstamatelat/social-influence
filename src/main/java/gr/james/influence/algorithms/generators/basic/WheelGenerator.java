package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * A generator that produces undirected wheel graphs.
 * <p>
 * The identification of this generator is
 * <ul>
 * <li>hub: the vertex on the center of the wheel</li>
 * </ul>
 *
 * @see <a href="http://mathworld.wolfram.com/WheelGraph.html">http://mathworld.wolfram.com/WheelGraph.html</a>
 */
public class WheelGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    private final int vertexCount;

    /**
     * Construct a new {@link WheelGenerator}.
     *
     * @param vertexCount the vertex count
     * @throws IllegalArgumentException if {@code vertexCount < 4}
     */
    public WheelGenerator(int vertexCount) {
        if (vertexCount < 4) {
            throw new IllegalArgumentException();
        }
        this.vertexCount = vertexCount;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r,
                                        VertexProvider<V> vertexProvider,
                                        Map<String, V> identification) {
        final DirectedGraph<V, E> g = new CycleGenerator<V, E>(vertexCount - 1).generate(r, vertexProvider);

        final V hub = g.addVertex(vertexProvider);
        for (V v : g) {
            if (!v.equals(hub)) {
                g.addEdge(v, hub);
                g.addEdge(hub, v);
            }
        }

        identification.put("hub", hub);

        assert g.vertexCount() == vertexCount;
        assert g.outDegree(hub) == vertexCount - 1;
        assert Graphs.getEdgesCount(g) == 4 * (vertexCount - 1);

        return g;
    }
}
