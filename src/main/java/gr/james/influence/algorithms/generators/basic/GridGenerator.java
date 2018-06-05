package gr.james.influence.algorithms.generators.basic;

import gr.james.influence.algorithms.generators.RandomGraphGenerator;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.VertexProvider;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>Generates a two-dimensional, undirected, n x m grid graph.</p>
 *
 * @see <a href="http://mathworld.wolfram.com/GridGraph.html">http://mathworld.wolfram.com/GridGraph.html</a>
 */
public class GridGenerator<V, E> implements RandomGraphGenerator<DirectedGraph<V, E>, V, E> {
    private int n, m;

    public GridGenerator(int n, int m) {
        this.n = n;
        this.m = m;
    }

    @Override
    public DirectedGraph<V, E> generate(Random r, VertexProvider<V> vertexProvider, Map<String, V> identification) {
        DirectedGraph<V, E> g = DirectedGraph.create();

        int count = 0;
        List<V> set = g.addVertices(vertexProvider, n * m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j != m - 1) {
                    g.addEdges(set.get(count), set.get(count + 1));
                }
                if (i != n - 1) {
                    g.addEdges(set.get(count), set.get(count + m));
                }
                count = count + 1;
            }
        }

        return g;
    }
}
