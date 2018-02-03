package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.AbstractIterativeAlgorithm;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

import java.util.HashMap;
import java.util.Map;

public class PageRank<V> extends AbstractIterativeAlgorithm<V, Double> {
    public static final double DEFAULT_PRECISION = -1.0;

    private double dampingFactor;
    private double epsilon;
    private Map<V, Double> outStrengths;

    public PageRank(DirectedGraph<V, ?> g, double dampingFactor, double epsilon) {
        super(g, GraphState.create(g.vertexSet(), 1.0));
        this.dampingFactor = dampingFactor;
        this.epsilon = epsilon;

        outStrengths = new HashMap<>();
        for (V v : g) {
            outStrengths.put(v, g.outStrength(v));
        }
    }

    public static <V> GraphState<V, Double> execute(DirectedGraph<V, ?> g, double dampingFactor, double epsilon) {
        return new PageRank<>(g, dampingFactor, epsilon).run();
    }

    public static <V> GraphState<V, Double> execute(DirectedGraph<V, ?> g, double dampingFactor) {
        return new PageRank<>(g, dampingFactor, DEFAULT_PRECISION).run();
    }

    @Override
    protected boolean converges(GraphState<V, Double> previous, GraphState<V, Double> next) {
        for (V v : next.keySet()) {
            if (Math.abs(next.get(v) - previous.get(v)) > epsilon) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected GraphState<V, Double> step(DirectedGraph<V, ?> g, GraphState<V, Double> previous) {
        final GraphState<V, Double> nextState = GraphState.create();
        for (V v : g) {
            double w = 0;
            for (DirectedEdge<V, ?> e : g.inEdges(v)) {
                w += e.weight() * previous.get(e.source()) / outStrengths.get(e.source());
            }
            nextState.put(v, dampingFactor + (1 - dampingFactor) * w);
        }
        return nextState;
    }
}
