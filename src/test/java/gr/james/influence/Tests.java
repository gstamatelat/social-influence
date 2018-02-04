package gr.james.influence;

import gr.james.influence.algorithms.distance.DijkstraDiameter;
import gr.james.influence.algorithms.generators.random.BarabasiAlbertGenerator;
import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.algorithms.generators.test.BarabasiAlbertClusterGenerator;
import gr.james.influence.algorithms.generators.test.TwoWheelsGenerator;
import gr.james.influence.algorithms.iterators.GraphStateIterator;
import gr.james.influence.algorithms.iterators.OrderedVertexIterator;
import gr.james.influence.algorithms.iterators.RandomSurferIterator;
import gr.james.influence.algorithms.scoring.DeGroot;
import gr.james.influence.algorithms.scoring.DegreeCentrality;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Graphs;
import gr.james.influence.graph.VertexProvider;
import gr.james.influence.util.Finals;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.collections.GraphState;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    /**
     * <p>PageRank values in a graph with N vertices must sum to N.</p>
     */
    @Test
    public void pageRankSum() {
        double dampingFactor = RandomHelper.getRandom().nextDouble();
        double p = RandomHelper.getRandom().nextDouble();
        int vertexCount = 40;

        /* Create graph and randomize edge weights */
        DirectedGraph<Integer, Object> g0 = new RandomGenerator<Integer, Object>(vertexCount, p).generate(VertexProvider.INTEGER_PROVIDER);
        Graphs.connect(g0);
        DirectedGraph<Integer, Object> g = Graphs.randomizeEdgeWeights(g0, true);

        /* PageRank values must sum to vertexCount */
        GraphState<Integer, Double> pr = PageRank.execute(g, dampingFactor);
        Assert.assertEquals("pageRankSum", 1.0, pr.getAverage(), 1.0e-2);
    }

    /**
     * <p>Test to demonstrate the equivalence between
     * {@link gr.james.influence.algorithms.iterators.RandomSurferIterator} and {@link PageRank} algorithm.</p>
     */
    @Test
    public void randomSurferTest() {
        int mean = 550000;
        double dampingFactor = RandomHelper.getRandom().nextDouble();
        double p = RandomHelper.getRandom().nextDouble();
        int vertexCount = 40;

        /* Create graph and randomize edge weights */
        DirectedGraph<Integer, Object> g0 = new RandomGenerator<Integer, Object>(vertexCount, p).generate(VertexProvider.INTEGER_PROVIDER);
        Graphs.connect(g0);
        DirectedGraph<Integer, Object> g = Graphs.randomizeEdgeWeights(g0, true);

        Finals.LOG.debug("damping factor = {}, p = {}", dampingFactor, p);

        /* Emulate the random surfer until mean of the map values average is MEAN, aka for MEAN * N steps */
        GraphState<Integer, Double> gs = GraphState.create(g.vertexSet(), 0.0);
        RandomSurferIterator<Integer, Object> rsi = new RandomSurferIterator<>(g, dampingFactor);
        int steps = mean * g.vertexCount();
        while (--steps > 0) {
            Integer v = rsi.next();
            gs.put(v, gs.get(v) + 1.0);
        }

        /* Get the PageRank and normalize gs to it */
        GraphState<Integer, Double> pr = PageRank.execute(g, dampingFactor);
        for (Integer v : gs.keySet()) {
            gs.put(v, pr.getSum() * gs.get(v) / (g.vertexCount() * mean));
        }

        /* Assert if maps not approx. equal with 1% error */
        for (Integer v : g) {
            Assert.assertEquals("randomSurferTest - " + g, 1.0, gs.get(v) / pr.get(v), 1.0e-2);
        }
    }

    /**
     * <p>This test demonstrates that in an undirected graph, eigenvector centrality vector and degree centrality vector
     * are proportional.</p>
     */
    @Test
    public void degreeEigenvectorTest() {
        int vertexCount = RandomHelper.getRandom().nextInt(240) + 10;

        /* Make the graph */
        DirectedGraph<Integer, Object> g = new BarabasiAlbertGenerator<Integer, Object>(vertexCount, 2, 2, 1.0).generate(VertexProvider.INTEGER_PROVIDER);

        /* Get PageRank and DegreeCentrality */
        GraphState<Integer, Integer> degree = DegreeCentrality.execute(g, Direction.INBOUND);
        GraphState<Integer, Double> pagerank = PageRank.execute(g, 0.0);

        /* Normalize pagerank */
        double mean = degree.getAverage();
        for (Integer v : g) {
            pagerank.put(v, pagerank.get(v) * mean);
        }

        /* Assert if maps not approx. equal */
        for (Integer v : g) {
            Assert.assertEquals("degreeEigenvectorTest - " + g, degree.get(v), pagerank.get(v), 1.0e-2);
        }
    }

    @Test
    public void combineGraphsTest() {
        int GRAPHS = 10;

        List<DirectedGraph<Integer, Object>> graphs = new ArrayList<>();
        //DirectedGraph<Integer, Object>[] graphs = (DirectedGraph<Integer, Object>) new DirectedGraph[GRAPHS];
        for (int i = 0; i < GRAPHS; i++) {
            int size = RandomHelper.getRandom().nextInt(50) + 50;
            graphs.add(new RandomGenerator<Integer, Object>(size, RandomHelper.getRandom().nextDouble()).generate(VertexProvider.INTEGER_PROVIDER));
            Graphs.connect(graphs.get(i));
        }

        int vertexCount = 0;
        int edgeCount = 0;
        for (DirectedGraph<Integer, Object> g : graphs) {
            vertexCount += g.vertexCount();
            edgeCount += Graphs.getEdgesCount(g);
        }

        DirectedGraph<Integer, Object> g = Graphs.combineGraphs(graphs);

        Assert.assertEquals("combineGraphsTest - vertexCount", vertexCount, g.vertexCount());
        Assert.assertEquals("combineGraphsTest - edgeCount", edgeCount, Graphs.getEdgesCount(g));
    }

    @Test
    public void clustersTest() {
        int clusters = RandomHelper.getRandom().nextInt(5) + 5;
        int clusterSize = RandomHelper.getRandom().nextInt(10) + 10;

        DirectedGraph<Integer, Object> g = new BarabasiAlbertClusterGenerator<Integer, Object>(clusterSize, 2, 2, 1.0, clusters).generate(VertexProvider.INTEGER_PROVIDER);
        Assert.assertEquals("clustersTest", clusters * clusterSize, g.vertexCount());
    }

    /**
     * <p>In the {@link TwoWheelsGenerator} graph, the maximum degree should be {@code max(6, n-1)}, where {@code n} is
     * the {@code int} input of {@link TwoWheelsGenerator#TwoWheelsGenerator(int)}</p>
     */
    @Test
    public void twoWheelsMaxDegreeTest() {
        int k = RandomHelper.getRandom().nextInt(100) + 4;

        /* Generate TwoWheels(k) */
        DirectedGraph<Integer, Object> g = new TwoWheelsGenerator<Integer, Object>(k).generate(VertexProvider.INTEGER_PROVIDER);

        /* Get max degree */
        int max = new GraphStateIterator<>(DegreeCentrality.execute(g, Direction.INBOUND)).next().weight;

        /* The max has to be k or 6 if k is too low */
        Assert.assertEquals("twoWheelsMaxDegreeTest - " + k, Math.max(6, k - 1), max);
    }

    /**
     * <p>Test for {@link MemoryGraph#getVertexFromIndex(int)} on the {@link TwoWheelsGenerator} graph.</p>
     */
    /*@Test
    public void getVertexFromIndexTest() {
        int k = RandomHelper.getRandom().nextInt(100) + 4;

        *//* Generate TwoWheels(k) *//*
        SimpleGraph g = new TwoWheelsGenerator(k).generate();

        *//* getVertexFromIndex(N) must always return the center vertex *//*
        Assert.assertEquals("getVertexFromIndexTest - N - " + k, 6, g.outDegree(g.getVertexFromIndex(g.getVerticesCount() - 1)));

        *//* getVertexFromIndex(N-1) must always return the wheel center vertex *//*
        Assert.assertEquals("getVertexFromIndexTest - N-1 - " + k, k - 1, g.outDegree(g.getVertexFromIndex(g.getVerticesCount() - 2)));
    }*/

    /**
     * <p>{@link OrderedVertexIterator} must return vertices ordered by ID.</p>
     */
    /*@Test
    public void orderedIteratorTest() {
        int size = RandomHelper.getRandom().nextInt(25) + 5;
        SimpleGraph g = new TwoWheelsGenerator(size).generate();
        OrderedVertexIterator<String> it = new OrderedVertexIterator<>(g);
        int total = 0;
        String pre = null;
        while (it.hasNext()) {
            String next = it.next();
            if (pre != null) {
                Assert.assertTrue("orderedIteratorTest - previous", next.getId() > pre.getId());
            }
            pre = next;
            total++;
        }
        Assert.assertEquals("orderedIteratorTest - length", g.getVerticesCount(), total);
    }*/

    /**
     * <p>In a graph without stubborn agents, all vertices reach to a common opinion upon DeGroot convergence.</p>
     */
    @Test
    public void deGrootTest() {
        int size = RandomHelper.getRandom().nextInt(50) + 50;
        double p = RandomHelper.getRandom().nextDouble();
        DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(size, p).generate(VertexProvider.INTEGER_PROVIDER);
        Graphs.connect(g);

        GraphState<Integer, Double> initialState = GraphState.create(g.vertexSet(), 0.0);
        for (Integer v : g) {
            initialState.put(v, RandomHelper.getRandom().nextDouble());
        }

        GraphState<Integer, Double> finalState = DeGroot.execute(g, initialState, 0.0);
        double avg = finalState.getAverage();

        for (double e : finalState.values()) {
            Assert.assertEquals("deGrootTest", avg, e, 1.0e-5);
        }
    }

    @Test
    public void deepCopyTest() {
        int size = RandomHelper.getRandom().nextInt(50) + 50;
        double p = RandomHelper.getRandom().nextDouble();
        DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(size, p).generate(VertexProvider.INTEGER_PROVIDER);
        Graphs.connect(g);
        DirectedGraph<Integer, Object> e = Graphs.deepCopy(g);
        e.addVertex(VertexProvider.INTEGER_PROVIDER);
        Assert.assertEquals("deepCopyTest", g.vertexCount() + 1, e.vertexCount());
        Assert.assertEquals("deepCopyTest", Graphs.getEdgesCount(g), Graphs.getEdgesCount(e));
    }

    @Test
    public void pageRankDeterministicTest() {
        int size = RandomHelper.getRandom().nextInt(50) + 50;
        double p = RandomHelper.getRandom().nextDouble();
        double dampingFactor = RandomHelper.getRandom().nextDouble();
        DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(size, p).generate(VertexProvider.INTEGER_PROVIDER);

        GraphState<Integer, Double> p1 = PageRank.execute(g, dampingFactor);
        GraphState<Integer, Double> p2 = PageRank.execute(g, dampingFactor);

        Assert.assertEquals("pageRankDeterministicTest", p1, p2);
    }

    @Test
    public void connectTest() {
        int size = RandomHelper.getRandom().nextInt(50) + 50;
        double p = RandomHelper.getRandom().nextDouble();
        DirectedGraph<Integer, Object> g = new RandomGenerator<Integer, Object>(size, p).generate(VertexProvider.INTEGER_PROVIDER);
        Graphs.connect(g);
        Assert.assertNotEquals("connectTest", Double.POSITIVE_INFINITY, new DijkstraDiameter<>(g).get());
    }
}
