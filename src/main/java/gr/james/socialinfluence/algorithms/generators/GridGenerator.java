package gr.james.socialinfluence.algorithms.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphFactory;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;

import java.util.List;

/**
 * <p>Generates a two-dimensional, undirected, n x m grid graph.</p>
 *
 * @see <a href="http://mathworld.wolfram.com/GridGraph.html">http://mathworld.wolfram.com/GridGraph.html</a>
 */
public class GridGenerator implements GraphGenerator {
    private int n, m;

    public GridGenerator(int n, int m) {
        this.n = n;
        this.m = m;
    }

    @Override
    public <T extends Graph> T create(GraphFactory<T> factory) {
        T g = factory.create();

        int count = 0;
        List<Vertex> set = g.addVertices(n * m);

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

        g.setGraphType("Grid");
        g.setMeta("n", String.valueOf(n));
        g.setMeta("m", String.valueOf(m));

        return g;
    }
}
