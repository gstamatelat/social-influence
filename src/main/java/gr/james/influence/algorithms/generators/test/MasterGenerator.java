package gr.james.influence.algorithms.generators.test;

import gr.james.influence.api.algorithms.GraphGenerator;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.api.graph.GraphFactory;
import gr.james.influence.util.Finals;

import java.util.Random;

/**
 * <p>Jackson, Matthew O. Social and economic networks. Vol. 3. Princeton: Princeton University Press, 2008, Figure
 * 8.3.2. A Society with a Convergent Updating Process.</p>
 */
public class MasterGenerator implements GraphGenerator {
    public MasterGenerator() {
    }

    @Override
    public <V, E> Graph<V, E> generate(GraphFactory<V, E> factory, Random r) {
        Graph<V, E> g = factory.createGraph();

        V v1 = g.addVertex();
        V v2 = g.addVertex();
        V v3 = g.addVertex();
        g.addEdge(v1, v3);
        g.addEdge(v1, v2);
        g.addEdge(v3, v2);
        g.addEdge(v2, v1);

        g.setMeta(Finals.TYPE_META, "Master");

        return g;
    }
}
