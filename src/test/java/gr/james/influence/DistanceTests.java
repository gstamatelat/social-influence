package gr.james.influence;

import com.google.common.math.LongMath;
import gr.james.influence.algorithms.distance.Dijkstra;
import gr.james.influence.algorithms.distance.FloydWarshall;
import gr.james.influence.algorithms.generators.basic.GridGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.collections.VertexPair;
import gr.james.influence.util.collections.VertexSequence;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class DistanceTests {
    /**
     * <p>This test demonstrates that {@link FloydWarshall} yields the same result as multiple executions of
     * {@link Dijkstra}.</p>
     */
    @Test
    public void floydWarshallTest() {
        int vertexCount = RandomHelper.getRandom().nextInt(250) + 10;
        double p = RandomHelper.getRandom().nextDouble();

        /* Create graph and randomize edge weights */
        Graph g = new RandomGenerator(vertexCount, p).generate();
        GraphUtils.connect(g);
        GraphUtils.randomizeEdgeWeights(g);

        /* Floyd-Warshall */
        Map<VertexPair, Double> distFloyd = FloydWarshall.execute(g);

        /* Dijkstra */
        Map<VertexPair, Double> distDijkstra = Dijkstra.executeDistanceMap(g);

        /* Length assertion */
        Assert.assertEquals("FloydWarshallTest - length - " + g, distFloyd.size(), distDijkstra.size());

        /* Value assertions */
        for (Vertex u : g) {
            for (Vertex v : g) {
                // TODO: Both Dijkstra and Floyd-Warshall use additions, it is intuitive that there won't be any double rounding issues
                // TODO: Also, 10^{-5} is too hardcoded for a quantity that could very well be really close to 10^{-5}
                // TODO: It's better to just compare 1 (one) with the ratio of distFloyd/distDijkstra
                Assert.assertEquals("FloydWarshallTest - " + g, distFloyd.get(new VertexPair(u, v)),
                        distDijkstra.get(new VertexPair(u, v)), 1.0e-5);
            }
        }
    }

    /**
     * <p>The amount of discrete shortest paths from one corner of a NxM grid graph to the distant one is
     * N+M-2 choose N-1</p>
     */
    @Test
    public void dijkstraPathsTest() {
        int n = 7;
        int m = 6;
        Graph g = new GridGenerator(n, m).generate();

        Map<VertexPair, Double> distFloyd = FloydWarshall.execute(g);

        VertexPair largestPair = distFloyd.keySet().iterator().next();
        double largestDistance = 0.0;
        for (VertexPair e : distFloyd.keySet()) {
            if (distFloyd.get(e) > largestDistance) {
                largestDistance = distFloyd.get(e);
                largestPair = e;
            }
        }

        Map<Vertex, Collection<VertexSequence>> r = Dijkstra.executeWithPath(g, largestPair.getSource());
        int num = r.get(largestPair.getTarget()).size();

        Assert.assertEquals("dijkstraPathsTest", LongMath.binomial(n + m - 2, n - 1), num);
    }
}
