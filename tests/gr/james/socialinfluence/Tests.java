package gr.james.socialinfluence;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.Degree;
import gr.james.socialinfluence.graph.algorithms.Dijkstra;
import gr.james.socialinfluence.graph.algorithms.FloydWarshall;
import gr.james.socialinfluence.graph.algorithms.PageRank;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.collections.VertexPair;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.graph.generators.RandomG;
import gr.james.socialinfluence.helper.Finals;
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
        double dampingFactors[] = {0, 0.3};
        double ps[] = {0.05, 0.1};

        for (int vertexCount = 20; vertexCount <= 40; vertexCount += 20) {
            for (double p : ps) {
                /* Create graph and randomize edge weights */
                Graph g = RandomG.generate(vertexCount, p);
                for (Edge e : g.getEdges()) {
                    e.setWeight(Finals.RANDOM.nextDouble());
                }

                /* For all damping factors */
                for (double DAMPING_FACTOR : dampingFactors) {
                    /* Emulate the random surfer until mean of the map values is MEAN, aka for MEAN * N steps */
                    GraphState gs = new GraphState(g, 0.0);
                    RandomSurferIterator rsi = new RandomSurferIterator(g, DAMPING_FACTOR);
                    int steps = mean * g.getVerticesCount();
                    while (--steps > 0) {
                        Vertex v = rsi.next();
                        gs.put(v, gs.get(v) + 1.0);
                    }

                    /* Get the PageRank and normalize gs to it */
                    GraphState pr = PageRank.execute(g, DAMPING_FACTOR);
                    for (Vertex v : gs.keySet()) {
                        gs.put(v, pr.getSum() * gs.get(v) / (g.getVerticesCount() * mean));
                    }

                    /* Assert if maps not approx. equal */
                    for (Vertex v : g.getVertices()) {
                        Assert.assertEquals("randomSurferTest - " + g.getMeta(), gs.get(v), pr.get(v), 0.01);
                    }
                }
            }
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
        int counts[] = {10, 20, 30, 40, 50, 100, 250};
        double ps[] = {0.05, 0.1, 0.2};

        for (int vertexCount : counts) {
            for (double p : ps) {
                /* Create graph and randomize edge weights */
                Graph g = RandomG.generate(vertexCount, p);
                for (Edge e : g.getEdges()) {
                    e.setWeight(Finals.RANDOM.nextDouble());
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

                /* Assertions */
                for (Vertex u : g.getVertices()) {
                    for (Vertex v : g.getVertices()) {
                        // TODO: Both Dijkstra and Floyd-Warshall use additions, it is intuitive that there won't be any double rounding issues
                        // TODO: Also, 10^{-5} is too hardcoded for a quantity that could very well be really close to 10^{-5}
                        // TODO: It's better to just compare 1 (one) with the ratio of distFloyd/distDijkstra
                        Assert.assertEquals("FloydWarshallTest - " + g.getMeta(), distFloyd.get(new VertexPair(u, v)), distDijkstra.get(new VertexPair(u, v)), Math.pow(10, -5));
                    }
                }
            }
        }
    }
}