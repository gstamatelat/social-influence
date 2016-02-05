package gr.james.influence.algorithms.distance;

import com.google.common.collect.BiMap;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;

import java.util.Collection;

/**
 * <p>Implementation of the <a href="https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm">
 * Tarjan's strongly connected components algorithm</a>.</p>
 */
public class Tarjan {
    public static Collection<Collection<Vertex>> execute(Graph g) {
        BiMap<Vertex, Integer> vertexMap = GraphUtils.getGraphIndexMap(g);

        return null;
    }
}
