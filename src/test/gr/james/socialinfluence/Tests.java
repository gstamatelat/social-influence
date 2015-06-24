package gr.james.socialinfluence;

import gr.james.socialinfluence.collections.GraphState;
import gr.james.socialinfluence.collections.VertexPair;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.*;
import gr.james.socialinfluence.graph.algorithms.iterators.InDegreeIterator;
import gr.james.socialinfluence.graph.algorithms.iterators.IndexIterator;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.graph.generators.BarabasiAlbertCluster;
import gr.james.socialinfluence.graph.generators.RandomG;
import gr.james.socialinfluence.graph.generators.TwoWheels;
import gr.james.socialinfluence.helper.RandomHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Tests {
    /**
     * <p>Test to demonstrate the equivalence between
     * {@link gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator} and
     * {@link gr.james.socialinfluence.graph.algorithms.PageRank} algorithm.</p>
     */
    @Test
    public void randomSurferTest() {
        int mean = 500000;
        double dampingFactor = RandomHelper.getRandom().nextDouble() * 0.3;
        double p = RandomHelper.getRandom().nextDouble() * 0.15 + 0.05;
        int vertexCount = 40;

        /* Create graph and randomize edge weights */
        Graph g = RandomG.generate(MemoryGraph.class, vertexCount, p);
        for (Edge e : g.getEdges()) {
            e.setWeight(RandomHelper.getRandom().nextDouble());
        }

        /* Emulate the random surfer until mean of the map values is MEAN, aka for MEAN * N steps */
        GraphState gs = new GraphState(g, 0.0);
        RandomSurferIterator rsi = new RandomSurferIterator(g, dampingFactor);
        int steps = mean * g.getVerticesCount();
        while (--steps > 0) {
            Vertex v = rsi.next();
            gs.put(v, gs.get(v) + 1.0);
        }

        /* Get the PageRank and normalize gs to it */
        GraphState pr = PageRank.execute(g, dampingFactor);
        for (Vertex v : gs.keySet()) {
            gs.put(v, pr.getSum() * gs.get(v) / (g.getVerticesCount() * mean));
        }

        /* Assert if maps not approx. equal */
        for (Vertex v : g.getVertices()) {
            Assert.assertEquals("randomSurferTest - " + g.getMeta(), gs.get(v), pr.get(v), 0.01);
        }
    }

    /**
     * <p>This test demonstrates that in an undirected graph, eigenvector centrality vector and degree centrality vector
     * are proportional.</p>
     */
    @Test
    public void degreeEigenvectorTest() {
        for (int vertexCount = 10; vertexCount < 250; vertexCount += 10) {
            /* Make the graph */
            Graph g = BarabasiAlbert.generate(vertexCount, 2, 2, 1.0);

            /* Get PageRank and Degree */
            GraphState degree = Degree.execute(g, true);
            GraphState pagerank = PageRank.execute(g, 0.0);

            /* Normalize degree */
            double mean = degree.getMean();
            for (Vertex v : g.getVertices()) {
                degree.put(v, degree.get(v) / mean);
            }

            /* Assert if maps not approx. equal */
            for (Vertex v : g.getVertices()) {
                Assert.assertEquals("degreeEigenvectorTest - " + g.getMeta(), degree.get(v), pagerank.get(v), 0.01);
            }
        }
    }

    /**
     * <p>This test demonstrates that {@link FloydWarshall} yields the same result as multiple executions of
     * {@link Dijkstra}.</p>
     */
    @Test
    public void floydWarshallTest() {
        int counts[] = {10, 20, 50, 100, 250};
        double ps[] = {0.05, 0.1, 0.2};

        for (int vertexCount : counts) {
            for (double p : ps) {
                /* Create graph and randomize edge weights */
                Graph g = RandomG.generate(MemoryGraph.class, vertexCount, p);
                for (Edge e : g.getEdges()) {
                    e.setWeight(RandomHelper.getRandom().nextDouble());
                }

                /* Floyd-Warshall */
                Map<VertexPair, Double> distFloyd = FloydWarshall.execute(g);

                /* Dijkstra */
                HashMap<VertexPair, Double> distDijkstra = new HashMap<VertexPair, Double>();
                for (Vertex v : g.getVertices()) {
                    HashMap<Vertex, Double> temp = Dijkstra.execute(g, v);
                    for (Map.Entry<Vertex, Double> e : temp.entrySet()) {
                        distDijkstra.put(new VertexPair(v, e.getKey()), e.getValue());
                    }
                }

                /* Length assertion */
                Assert.assertEquals("FloydWarshallTest - length - " + g.getMeta(), distFloyd.size(), distDijkstra.size());

                /* Value assertions */
                for (Vertex u : g.getVertices()) {
                    for (Vertex v : g.getVertices()) {
                        // TODO: Both Dijkstra and Floyd-Warshall use additions, it is intuitive that there won't be any double rounding issues
                        // TODO: Also, 10^{-5} is too hardcoded for a quantity that could very well be really close to 10^{-5}
                        // TODO: It's better to just compare 1 (one) with the ratio of distFloyd/distDijkstra
                        Assert.assertEquals("FloydWarshallTest - " + g.getMeta(), distFloyd.get(new VertexPair(u, v)),
                                distDijkstra.get(new VertexPair(u, v)), Math.pow(10, -5));
                    }
                }
            }
        }
    }

    @Test
    public void combineGraphsTest() {
        int GRAPHS = 10;

        MemoryGraph[] graphs = new MemoryGraph[GRAPHS];
        for (int i = 0; i < GRAPHS; i++) {
            graphs[i] = (MemoryGraph) RandomG.generate(MemoryGraph.class, RandomHelper.getRandom().nextInt(50) + 50, RandomHelper.getRandom().nextDouble());
        }

        int vertexCount = 0;
        int edgeCount = 0;
        for (MemoryGraph g : graphs) {
            vertexCount += g.getVerticesCount();
            edgeCount += g.getEdgesCount();
        }

        MemoryGraph g = MemoryGraph.combineGraphs(graphs);

        Assert.assertEquals("combineGraphsTest - vertexCount", vertexCount, g.getVerticesCount());
        Assert.assertEquals("combineGraphsTest - edgeCount", edgeCount, g.getEdgesCount());

        for (MemoryGraph u : graphs) {
            Assert.assertEquals("combineGraphsTest - subGraph - vertices", 0, u.getVerticesCount());
            Assert.assertEquals("combineGraphsTest - subGraph - edges", 0, u.getEdgesCount());
        }
    }

    @Test
    public void clustersTest() {
        int[] clusters = {5, 6, 7, 8, 9, 10};
        int[] clusterSize = {10, 15, 20};

        for (int _clusters : clusters) {
            for (int _clusterSize : clusterSize) {
                Graph g = BarabasiAlbertCluster.generate(_clusterSize, 2, 2, 1.0, _clusters);
                Assert.assertEquals("clustersTest", _clusters * _clusterSize, g.getVerticesCount());
            }
        }
    }

    /**
     * <p>In the {@link TwoWheels} graph, the maximum degree should be {@code max(6, n-1)}, where {@code n} is the input
     * of {@link TwoWheels#generate(int)}</p>
     */
    @Test
    public void twoWheelsMaxDegreeTest() {
        for (int k = 4; k < 100; k++) {
            /* Generate TwoWheels(k) */
            Graph g = TwoWheels.generate(k);

            /* Get max degree */
            int max = new InDegreeIterator(g).next().getOutDegree();

            /* The max has to be k or 6 if k is too low */
            Assert.assertEquals("twoWheelsMaxDegreeTest - " + k, Math.max(6, k - 1), max);
        }
    }

    /**
     * <p>Test for {@link MemoryGraph#getVertexFromIndex(int)} on the {@link TwoWheels} graph.</p>
     */
    @Test
    public void getVertexFromIndexTest() {
        for (int k = 4; k < 100; k++) {
            /* Generate TwoWheels(k) */
            Graph g = TwoWheels.generate(k);

            /* Get max degree */
            int max = new InDegreeIterator(g).next().getOutDegree();

            /* getVertexFromIndex(N) must always return the center vertex */
            Assert.assertEquals("getVertexFromIndexTest - " + k, 6, g.getVertexFromIndex(g.getVerticesCount() - 1).getOutDegree());

            /* getVertexFromIndex(N-1) must always return the wheel center vertex */
            Assert.assertEquals("getVertexFromIndexTest - " + k, k - 1, g.getVertexFromIndex(g.getVerticesCount() - 2).getOutDegree());
        }
    }

    @Test
    public void indexIteratorTest() {
        Graph g = TwoWheels.generate(RandomHelper.getRandom().nextInt(25) + 5);
        IndexIterator it = new IndexIterator(g);
        int total = 0;
        Vertex pre = null;
        while (it.hasNext()) {
            Vertex next = it.next();
            if (pre != null) {
                Assert.assertTrue("indexIteratorTest - previous", next.getId() > pre.getId());
            }
            pre = next;
            total++;
        }
        Assert.assertEquals("indexIteratorTest - length", g.getVerticesCount(), total);
    }

    /**
     * <p>In a graph without stubborn agents, all vertices reach to a common opinion upon DeGroot convergence.</p>
     */
    @Test
    public void deGrootTest() {
        Graph g = RandomG.generate(MemoryGraph.class, 100, 0.05);

        GraphState initialState = new GraphState(g, 0.0);
        for (Vertex v : g.getVertices()) {
            initialState.put(v, RandomHelper.getRandom().nextDouble());
        }

        GraphState finalState = DeGroot.execute(g, initialState, 0.0, true);
        double avg = finalState.getMean();

        for (double e : finalState.values()) {
            Assert.assertEquals("deGrootTest", avg, e, 0.00001);
        }
    }
}