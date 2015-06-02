package gr.james.socialinfluence.tests;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.PageRank;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.graph.generators.RandomG;
import gr.james.socialinfluence.helper.Finals;

/**
 * <p>Test class to demonstrate the equivalence between
 * {@link gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator} and
 * {@link gr.james.socialinfluence.graph.algorithms.PageRank} algorithm.</p>
 */
public class RandomSurferTest {
    public static void main(String[] args) {
        /**
         * Variables: You should change the following variables: MEAN, DAMPING_FACTOR and g as you see fit. The results
         * printed at the end must always be identical (or almost identical).
         *
         * MEAN:            Controls how many steps the random surfer does.
         * DAMPING_FACTOR:  Probability of a random jump.
         * g:               The graph.
         */
        double MEAN = 1000000;
        double DAMPING_FACTOR = 0.15;
        Graph g = RandomG.generate(10, 0.2);

        for (Edge e : g.getEdges()) {
            e.setWeight(Finals.RANDOM.nextDouble());
        }

        GraphState gs = new GraphState(g, 0.0);
        GraphState pr = PageRank.execute(g, DAMPING_FACTOR);

        RandomSurferIterator rsi = new RandomSurferIterator(g, DAMPING_FACTOR);
        while (gs.getMean() < MEAN) {
            Vertex v = rsi.next();
            gs.put(v, gs.get(v) + 1.0);
        }

        for (Vertex v : gs.keySet()) {
            gs.put(v, pr.getSum() * gs.get(v) / (g.getVerticesCount() * MEAN));
        }

        System.out.println("PageRank: " + pr);
        System.out.println("RandomSurferIterator: " + gs);
    }
}