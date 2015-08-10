package gr.james.socialinfluence;

import gr.james.socialinfluence.algorithms.generators.*;
import gr.james.socialinfluence.algorithms.iterators.GraphStateIterator;
import gr.james.socialinfluence.algorithms.iterators.OrderedVertexIterator;
import gr.james.socialinfluence.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.algorithms.scoring.DeGroot;
import gr.james.socialinfluence.algorithms.scoring.Degree;
import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.RandomHelper;
import gr.james.socialinfluence.util.collections.VertexPair;
import gr.james.socialinfluence.util.states.DoubleGraphState;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class Tests {
    /**
     * <p>PageRank values in a graph with N vertices must sum to N.</p>
     */
    @Test
    public void pageRankSum() {
        double dampingFactor = RandomHelper.getRandom().nextDouble() * 0.3;
        double p = RandomHelper.getRandom().nextDouble() * 0.2 + 0.1;
        int vertexCount = 40;

        /* Create graph and randomize edge weights */
        Graph g = new RandomGenerator<>(MemoryGraph.class, vertexCount, p).create();
        GraphUtils.createCircle(g, true);
        for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            e.getValue().setWeight(RandomHelper.getRandom().nextDouble());
        }

        /* PageRank values must sum to vertexCount */
        GraphState<Double> pr = PageRank.execute(g, dampingFactor);
        Assert.assertEquals("pageRankSum", 1.0, pr.getMean(), 1.0e-2);
    }

    /**
     * <p>Test to demonstrate the equivalence between
     * {@link gr.james.socialinfluence.algorithms.iterators.RandomSurferIterator} and
     * {@link PageRank} algorithm.</p>
     */
    @Test
    public void randomSurferTest() {
        int mean = 500000;
        double dampingFactor = RandomHelper.getRandom().nextDouble() * 0.3;
        double p = RandomHelper.getRandom().nextDouble() * 0.2 + 0.1;
        int vertexCount = 40;

        /* Create graph and randomize edge weights */
        Graph g = new RandomGenerator<>(MemoryGraph.class, vertexCount, p).create();
        GraphUtils.createCircle(g, true);
        for (Map.Entry<VertexPair, Edge> e : g.getEdges().entrySet()) {
            e.getValue().setWeight(RandomHelper.getRandom().nextDouble());
        }

        /* Emulate the random surfer until mean of the map values average is MEAN, aka for MEAN * N steps */
        GraphState<Double> gs = new DoubleGraphState(g, 0.0);
        RandomSurferIterator rsi = new RandomSurferIterator(g, dampingFactor);
        int steps = mean * g.getVerticesCount();
        while (--steps > 0) {
            Vertex v = rsi.next();
            gs.put(v, gs.get(v) + 1.0);
        }

        /* Get the PageRank and normalize gs to it */
        GraphState<Double> pr = PageRank.execute(g, dampingFactor);
        for (Vertex v : gs.keySet()) {
            gs.put(v, pr.getSum() * gs.get(v) / (g.getVerticesCount() * mean));
        }

        /* Assert if maps not approx. equal with 1% error */
        for (Vertex v : g) {
            Assert.assertEquals("randomSurferTest - " + g, 1.0, gs.get(v) / pr.get(v), 1.0e-2);
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
            Graph g = new BarabasiAlbertGenerator<>(MemoryGraph.class, vertexCount, 2, 2, 1.0).create();

            /* Get PageRank and Degree */
            GraphState<Integer> degree = Degree.execute(g, true);
            GraphState<Double> pagerank = PageRank.execute(g, 0.0);

            /* Normalize pagerank */
            double mean = degree.getMean();
            for (Vertex v : g) {
                pagerank.put(v, pagerank.get(v) * mean);
            }

            /* Assert if maps not approx. equal */
            for (Vertex v : g) {
                Assert.assertEquals("degreeEigenvectorTest - " + g, degree.get(v), pagerank.get(v), 1.0e-2);
            }
        }
    }

    @Test
    public void lessisTest() {
        Graph g = new CycleGenerator<>(MemoryGraph.class, 3).create();

        Game game = new Game(g);

        Move m1 = new Move();
        m1.putVertex(g.getVertexFromIndex(0), 1.5);
        m1.putVertex(g.getVertexFromIndex(1), 1.5);

        Move m2 = new Move();
        m2.putVertex(g.getVertexFromIndex(0), 0.5);
        m2.putVertex(g.getVertexFromIndex(1), 0.5);
        m2.putVertex(g.getVertexFromIndex(2), 2.0);

        game.setPlayerAMove(m1);
        game.setPlayerBMove(m2);

        GameResult r = game.runGame(new GameDefinition(3, 3.0, 50000L), 0.0);
        Assert.assertEquals("lessisTest", 0, r.score);
    }

    @Test
    public void combineGraphsTest() {
        int GRAPHS = 10;

        Graph[] graphs = new Graph[GRAPHS];
        for (int i = 0; i < GRAPHS; i++) {
            graphs[i] = new RandomGenerator<>(MemoryGraph.class, RandomHelper.getRandom().nextInt(50) + 50, RandomHelper.getRandom().nextDouble()).create();
            GraphUtils.createCircle(graphs[i], true);
        }

        int vertexCount = 0;
        int edgeCount = 0;
        for (Graph g : graphs) {
            vertexCount += g.getVerticesCount();
            edgeCount += g.getEdgesCount();
        }

        Graph g = GraphUtils.combineGraphs(MemoryGraph.class, graphs);

        Assert.assertEquals("combineGraphsTest - vertexCount", vertexCount, g.getVerticesCount());
        Assert.assertEquals("combineGraphsTest - edgeCount", edgeCount, g.getEdgesCount());
    }

    @Test
    public void clustersTest() {
        int[] clusters = {5, 6, 7, 8, 9, 10};
        int[] clusterSize = {10, 15, 20};

        for (int _clusters : clusters) {
            for (int _clusterSize : clusterSize) {
                Graph g = new BarabasiAlbertClusterGenerator<>(MemoryGraph.class, _clusterSize, 2, 2, 1.0, _clusters).create();
                Assert.assertEquals("clustersTest", _clusters * _clusterSize, g.getVerticesCount());
            }
        }
    }

    /**
     * <p>In the {@link TwoWheelsGenerator} graph, the maximum degree should be {@code max(6, n-1)}, where {@code n} is
     * the {@code int} input of {@link TwoWheelsGenerator#TwoWheelsGenerator(Class, int)}</p>
     */
    @Test
    public void twoWheelsMaxDegreeTest() {
        for (int k = 4; k < 100; k++) {
            /* Generate TwoWheels(k) */
            Graph g = new TwoWheelsGenerator<>(MemoryGraph.class, k).create();

            /* Get max degree */
            int max = new GraphStateIterator<>(Degree.execute(g, true)).next().getWeight();

            /* The max has to be k or 6 if k is too low */
            Assert.assertEquals("twoWheelsMaxDegreeTest - " + k, Math.max(6, k - 1), max);
        }
    }

    /**
     * <p>Test for {@link MemoryGraph#getVertexFromIndex(int)} on the {@link TwoWheelsGenerator} graph.</p>
     */
    @Test
    public void getVertexFromIndexTest() {
        for (int k = 4; k < 100; k++) {
            /* Generate TwoWheels(k) */
            Graph g = new TwoWheelsGenerator<>(MemoryGraph.class, k).create();

            /* getVertexFromIndex(N) must always return the center vertex */
            Assert.assertEquals("getVertexFromIndexTest - N - " + k, 6, g.getOutDegree(g.getVertexFromIndex(g.getVerticesCount() - 1)));

            /* getVertexFromIndex(N-1) must always return the wheel center vertex */
            Assert.assertEquals("getVertexFromIndexTest - N-1 - " + k, k - 1, g.getOutDegree(g.getVertexFromIndex(g.getVerticesCount() - 2)));
        }
    }

    @Test
    public void indexIteratorTest() {
        Graph g = new TwoWheelsGenerator<>(MemoryGraph.class, RandomHelper.getRandom().nextInt(25) + 5).create();
        OrderedVertexIterator it = new OrderedVertexIterator(g);
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
        Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.1).create();
        GraphUtils.createCircle(g, true);

        GraphState<Double> initialState = new DoubleGraphState(g, 0.0);
        for (Vertex v : g) {
            initialState.put(v, RandomHelper.getRandom().nextDouble());
        }

        GraphState<Double> finalState = DeGroot.execute(g, initialState, 0.0, true);
        double avg = finalState.getMean();

        for (double e : finalState.values()) {
            Assert.assertEquals("deGrootTest", avg, e, 1.0e-5);
        }
    }

    @Test
    public void deepCopyTest() {
        Graph g = new RandomGenerator<>(MemoryGraph.class, 100, 0.05).create();
        GraphUtils.createCircle(g, true);
        Graph e = GraphUtils.deepCopy(MemoryGraph.class, g);
        e.addVertex();
        Assert.assertEquals("deepCopyTest", g.getVerticesCount() + 1, e.getVerticesCount());
        Assert.assertEquals("deepCopyTest", g.getEdgesCount(), e.getEdgesCount());
    }
}
