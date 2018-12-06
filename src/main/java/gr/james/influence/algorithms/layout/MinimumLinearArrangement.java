package gr.james.influence.algorithms.layout;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.graph.UndirectedEdge;
import gr.james.influence.graph.UndirectedGraph;

import java.util.*;

/**
 * A local search algorithm for the problem of Minimum Linear Arrangement.
 *
 * <h2>How to use:</h2>
 * <pre><code>
 * LinearArrangement&lt;String&gt; minla = new LinearArrangement&lt;&gt;(g, new Random()); // Instantiate
 * minla.run(); // Run for one time, or more times if you want
 * System.out.println(minla.minimumLinearArrangement()); // Print the best arrangement
 * System.out.println(minla.minimumLinearArrangementCost()); // And it's cost
 * </code></pre>
 *
 * @param <V> the vertex type
 */
public class MinimumLinearArrangement<V> {
    private final UndirectedGraph<V, ?> g;
    private final Random r;
    private List<V> minla;
    private double cost;

    /**
     * Construct a new {@link MinimumLinearArrangement} from an {@link UndirectedGraph} and a random seed.
     * <p>
     * The graph {@code g} must not be mutated after the constructor has been invoked.
     *
     * @param g the graph
     * @param r the random seed
     */
    public MinimumLinearArrangement(@UnmodifiableGraph UndirectedGraph<V, ?> g, Random r) {
        this.g = g;
        this.r = r;

        this.minla = new ArrayList<>(g.vertexSet());
        Collections.shuffle(minla, r);
        cost = calculateScore(g, minla);

        System.out.printf("%f: %s%n", cost, minla); // If you don't like the output, you can suppress it
    }

    /**
     * Helper method that returns the cost of a given arrangement for a graph.
     *
     * @param g           the graph
     * @param arrangement the linear arrangement
     * @param <V>         the vertex type
     * @return the cost of {@code arrangement} given the graph {@code g}
     */
    public static <V> double calculateScore(UndirectedGraph<V, ?> g, List<V> arrangement) {
        double score = 0;
        final Map<V, Integer> pos = new HashMap<>(arrangement.size());
        for (int i = 0; i < arrangement.size(); i++) {
            pos.put(arrangement.get(i), i);
        }
        for (UndirectedEdge<V, ?> edge : g.edges()) {
            score += edge.weight() * Math.abs(pos.get(edge.v()) - pos.get(edge.w()));
        }
        return score;
    }

    /**
     * Run the algorithm for one time.
     * <p>
     * The algorithm will try to find a better solution during each run.
     */
    public void run() {
        final List<V> order = new ArrayList<>(g.vertexSet());
        Collections.shuffle(order, r);

        // Fast converge
        double minCost = calculateScore(g, order);
        for (int i = 0; i < g.vertexCount() * g.vertexCount(); i++) {
            final int x = r.nextInt(g.vertexCount());
            final int y = r.nextInt(g.vertexCount());
            Collections.swap(order, x, y);
            final double newCost = calculateScore(g, order);
            if (newCost < minCost) {
                minCost = newCost;
            } else {
                Collections.swap(order, x, y);
            }
        }

        // Local minimum
        boolean changed;
        do {
            changed = false;
            for (int x = 0; x < g.vertexCount() - 1; x++) {
                for (int y = x + 1; y < g.vertexCount(); y++) {
                    Collections.swap(order, x, y);
                    final double newCost = calculateScore(g, order);
                    if (newCost < minCost) {
                        minCost = newCost;
                        changed = true;
                    } else {
                        Collections.swap(order, x, y);
                    }
                }
            }
        } while (changed);

        if (minCost < cost) {
            cost = minCost;
            minla = order;
        }

        System.out.printf("%f: %s%n", cost, minla); // If you don't like the output, you can suppress it
    }

    /**
     * Run the algorithm for multiple times.
     *
     * @param times how many times to run the algorithm for
     */
    public void runForTimes(int times) {
        if (times < 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < times; i++) {
            run();
        }
    }

    /**
     * Run the algorithm for a specific amount of minutes.
     *
     * @param minutes how many minutes to run the algorithm for
     */
    public void runForMinutes(double minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException();
        }
        long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now < minutes * 60 * 1000) {
            run();
        }
    }

    /**
     * Run the algorithm forever.
     * <p>
     * Normally not very useful, but you can watch the output.
     */
    public void runForever() {
        while (true) {
            run();
        }
    }

    /**
     * Returns the minimum cost arrangement that have been stored so far.
     *
     * @return the minimum cost arrangement that have been stored so far
     */
    public List<V> minimumLinearArrangement() {
        return Collections.unmodifiableList(minla);
    }

    /**
     * Returns the cost of {@link #minimumLinearArrangement()}.
     *
     * @return the cost of {@link #minimumLinearArrangement()}
     */
    public double minimumLinearArrangementCost() {
        return cost;
    }
}
