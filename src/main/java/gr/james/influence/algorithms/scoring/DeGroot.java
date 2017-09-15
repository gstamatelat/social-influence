package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.AbstractIterativeAlgorithm;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.collections.GraphState;

import java.util.Map;

public class DeGroot<V> extends AbstractIterativeAlgorithm<V, Double> {
    public static final double DEFAULT_PRECISION = -1.0;

    private double epsilon;

    public DeGroot(Graph<V, ?> g, GraphState<V, Double> initial, double epsilon) {
        super(g, initial);
        this.epsilon = epsilon;
    }

    public static <V> GraphState<V, Double> execute(Graph<V, ?> g, GraphState<V, Double> initial, double epsilon) {
        return new DeGroot<>(g, initial, epsilon).run();
    }

    public static <V> GraphState<V, Double> execute(Graph<V, ?> g, GraphState<V, Double> initial) {
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
    protected GraphState<V, Double> step(Graph<V, ?> g, GraphState<V, Double> previous) {
        GraphState<V, Double> nextState = GraphState.create();
        for (V v : g) {
            double vNewValue = 0.0;
            for (Map.Entry<V, ? extends GraphEdge<V, ?>> e : g.getOutEdges(v).entrySet()) {
                vNewValue = vNewValue + (
                        e.getValue().getWeight() * previous.get(e.getKey())
                );
            }
            nextState.put(v, vNewValue / g.getOutStrength(v));
        }
        return nextState;
    }
}
