package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.AbstractIterativeAlgorithm;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

public class DeGroot<V> extends AbstractIterativeAlgorithm<V, Double> {
    public static final double DEFAULT_PRECISION = -1.0;

    private double epsilon;

    public DeGroot(DirectedGraph<V, ?> g, GraphState<V, Double> initial, double epsilon) {
        super(g, initial);
        this.epsilon = epsilon;
    }

    public static <V> GraphState<V, Double> execute(DirectedGraph<V, ?> g, GraphState<V, Double> initial, double epsilon) {
        return new DeGroot<>(g, initial, epsilon).run();
    }

    public static <V> GraphState<V, Double> execute(DirectedGraph<V, ?> g, GraphState<V, Double> initial) {
        return new DeGroot<>(g, initial, DEFAULT_PRECISION).run();
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
            double w = 0.0;
            for (DirectedEdge<V, ?> e : g.outEdges(v)) {
                w += e.weight() * previous.get(e.target());
            }
            nextState.put(v, w / g.outStrength(v));
        }
        return nextState;
    }
}
