package gr.james.influence.algorithms.generators.test;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.graph.Graph;
import gr.james.influence.graph.MutableGraph;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Finals;

import java.util.Map;
import java.util.Random;

/**
 * <p>Jackson, Matthew O. Social and economic networks. Vol. 3. Princeton: Princeton University Press, 2008, Figure
 * 8.3.2. A Society with a Convergent Updating Process.</p>
 */
public class MasterGenerator<V, E> implements GraphGenerator<Graph<V, E>, V, E> {
    public MasterGenerator() {
    }

    @Override
    public Graph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        Graph<V, E> g = new MutableGraph<>();

        V v1 = g.addVertex(vertexProvider);
        V v2 = g.addVertex(vertexProvider);
        V v3 = g.addVertex(vertexProvider);
        g.addEdge(v1, v3);
        g.addEdge(v1, v2);
        g.addEdge(v3, v2);
        g.addEdge(v2, v1);

        g.setMeta(Finals.TYPE_META, "Master");

        return g;
    }
}
