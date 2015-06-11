package gr.james.socialinfluence.tests;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.PageRank;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.graph.collections.GraphState;
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
    public void randomSurferTest() throws Exception {
        /**
         * Variables: You should change the following variables: MEAN, DAMPING_FACTORS and g as you see fit. The results
         * printed at the end must always be identical (or almost identical).
         *
         * MEAN:            Controls how many steps the random surfer does.
         * DAMPING_FACTOR:  Probability of a random jump.
         * g:               The graph.
         */

        // TODO: Execute this for multiple graphs
        double MEAN = 1000000;
        double DAMPING_FACTORS[] = {0.15, 0.3, 0.5};
        Graph G = RandomG.generate(20, 0.2);

        for (double DAMPING_FACTOR : DAMPING_FACTORS) {
            /* Randomize edge weights */
            for (Edge e : G.getEdges()) {
                e.setWeight(Finals.RANDOM.nextDouble());
            }

            /* Emulate the random surfer until mean of the map values is MEAN, aka for MEAN * N steps */
            GraphState gs = new GraphState(G, 0.0);
            RandomSurferIterator rsi = new RandomSurferIterator(G, DAMPING_FACTOR);
            while (gs.getMean() < MEAN) {
                Vertex v = rsi.next();
                gs.put(v, gs.get(v) + 1.0);
            }

            /* Get the PageRank and normalize gs to it */
            GraphState pr = PageRank.execute(G, DAMPING_FACTOR);
            for (Vertex v : gs.keySet()) {
                gs.put(v, pr.getSum() * gs.get(v) / (G.getVerticesCount() * MEAN));
            }

            /* Assert if maps not approx. equal */
            for (Vertex v : G.getVertices()) {
                Assert.assertEquals("PageRank value and RandomSurferIterator must create the same centrality", gs.get(v), pr.get(v), 0.01);
            }
        }
    }
}