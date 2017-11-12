package gr.james.influence;

import com.google.common.math.LongMath;
import gr.james.influence.algorithms.distance.Dijkstra;
import gr.james.influence.algorithms.distance.FloydWarshall;
import gr.james.influence.algorithms.generators.basic.GridGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.SimpleGraph;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.collections.VertexPair;
import gr.james.influence.util.collections.VertexSequence;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class DistanceTests {
    /**
     * The {@link FloydWarshall} algorithm yields the same result as multiple executions of {@link Dijkstra}.
     */
    @Test
    public void floydWarshallTest() {
        int vertexCount = RandomHelper.getRandom().nextInt(250) + 10;
        double p = RandomHelper.getRandom().nextDouble();

        /* Create graph and randomize edge weights */
        SimpleGraph g = new RandomGenerator(vertexCount, p).generate();
        Graphs.connect(g);
        Graphs.randomizeEdgeWeights(g);

        /* Floyd-Warshall */
        Map<VertexPair<String>, Double> distFloyd = FloydWarshall.execute(g);

        /* Dijkstra */
        Map<VertexPair<String>, Double> distDijkstra = Dijkstra.executeDistanceMap(g);

        /* Length assertion */
        Assert.assertEquals("DistanceTests.floydWarshallTest", distFloyd.size(), distDijkstra.size());

        /* Value assertions */
        for (String u : g) {
            for (String v : g) {
                if (!Objects.equals(v, u)) {
                    final double ratio = distFloyd.get(new VertexPair<>(u, v)) / distDijkstra.get(new VertexPair<>(u, v));
                    Assert.assertEquals("DistanceTests.floydWarshallTest", 1.0, ratio, 1.0e-4);
                } else {
                    Assert.assertTrue("DistanceTests.floydWarshallTest",
                            distFloyd.get(new VertexPair<>(u, v)) == 0);
                    Assert.assertTrue("DistanceTests.floydWarshallTest",
                            distDijkstra.get(new VertexPair<>(u, v)) == 0);
                }
            }
        }
    }

    /**
     * The amount of discrete shortest paths from one corner of a NxM grid graph to the distant one is N+M-2 choose N-1.
     */
    @Test
    public void dijkstraPathsTest() {
        int n = 7;
        int m = 6;
        SimpleGraph g = new GridGenerator(n, m).generate();

        Map<VertexPair<String>, Double> distFloyd = FloydWarshall.execute(g);

        VertexPair<String> largestPair = distFloyd.keySet().iterator().next();
        double largestDistance = 0.0;
        for (VertexPair<String> e : distFloyd.keySet()) {
            if (distFloyd.get(e) > largestDistance) {
                largestDistance = distFloyd.get(e);
                largestPair = e;
            }
        }

        Map<String, Collection<VertexSequence<String>>> r = Dijkstra.executeWithPath(g, largestPair.getSource());
        int num = r.get(largestPair.getTarget()).size();

        Assert.assertEquals("DistanceTests.dijkstraPathsTest", LongMath.binomial(n + m - 2, n - 1), num);
    }
}
