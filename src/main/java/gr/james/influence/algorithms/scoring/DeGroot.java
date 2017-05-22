package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.scoring.util.IterativeAlgorithmHelper;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;

import java.util.Map;

public class DeGroot {
    public static <V, E> GraphState<V, Double> execute(Graph<V, E> g, GraphState<V, Double> initialOpinions, double epsilon) {
        return IterativeAlgorithmHelper.execute(
                g,
                initialOpinions,
                oldState -> {
                    GraphState<V, Double> nextState = new GraphState<>();
                    for (V v : g) {
                        double vNewValue = 0.0;
                        for (Map.Entry<V, GraphEdge<V, E>> e : g.getOutEdges(v).entrySet()) {
                            vNewValue = vNewValue + (
                                    e.getValue().getWeight() * oldState.get(e.getKey())
                            );
                        }
                        nextState.put(v, vNewValue / g.getOutStrength(v));
                    }
                    return nextState;
                },
                (t1, t2, e) -> Math.abs(t1 - t2) <= e,
                epsilon
        );
    }

    public static <V> GraphState<V, Double> execute(Graph<V, ?> g, GraphState<V, Double> initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION);
    }
}
