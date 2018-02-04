package gr.james.influence.algorithms.generators.test;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;

import java.util.Map;
import java.util.Random;

/**
 * <p>Jackson, Matthew O. Social and economic networks. Vol. 3. Princeton: Princeton University Press, 2008, Figure
 * 8.3.2. A Society with a Convergent Updating Process.</p>
 */
public class MasterGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    public MasterGenerator() {
    }

    @Override
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

        V v1 = g.addVertex(vertexProvider);
        V v2 = g.addVertex(vertexProvider);
        V v3 = g.addVertex(vertexProvider);
        g.addEdge(v1, v3);
        g.addEdge(v1, v2);
        g.addEdge(v3, v2);
        g.addEdge(v2, v1);

        return g;
    }
}
