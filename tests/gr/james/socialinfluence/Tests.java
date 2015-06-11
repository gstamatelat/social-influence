package gr.james.socialinfluence;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.Degree;
import gr.james.socialinfluence.graph.algorithms.PageRank;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.generators.BarabasiAlbert;
import gr.james.socialinfluence.graph.generators.RandomG;
import gr.james.socialinfluence.helper.Finals;
import org.junit.Assert;
import org.junit.Test;

public class Tests {
    /**
     * <p>Test to demonstrate the equivalence between
     * {@link gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator} and
     * {@link gr.james.socialinfluence.graph.algorithms.PageRank} algorithm.</p>
     */
    @Test
    public void randomSurferTest() {
        double mean = 1000000;
        double dampingFactors[] = {0, 0.3};
        double ps[] = {0.05, 0.1};

        for (int vertexCount = 10; vertexCount < 90; vertexCount += 20) {
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
                    while (gs.getMean() < mean) {
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
                        Assert.assertEquals("randomSurferTest", gs.get(v), pr.get(v), 0.01);
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
}