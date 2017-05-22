package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.scoring.util.IterativeAlgorithmHelper;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;

import java.util.HashMap;
import java.util.Map;

public class PageRank {
    public static <V, E> GraphState<V, Double> execute(Graph<V, E> g, double dampingFactor, double epsilon) {
        HashMap<V, Double> outStrengths = new HashMap<>();
        for (V v : g) {
            outStrengths.put(v, g.getOutStrength(v));
        }

        return IterativeAlgorithmHelper.execute(
                g,
                new GraphState<>(g, 1.0),
                oldState -> {
                    GraphState<V, Double> nextState = new GraphState<>(g, 0.0);
                    for (V v : g) {
                        Map<V, GraphEdge<V, E>> inEdges = g.getInEdges(v);
                        for (Map.Entry<V, GraphEdge<V, E>> e : inEdges.entrySet()) {
                            nextState.put(v, nextState.get(v) +
                                    e.getValue().getWeight() * oldState.get(e.getKey()) / outStrengths.get(e.getKey()));
                        }
                    }
                    for (Map.Entry<V, Double> k : nextState.entrySet()) {
                        k.setValue(dampingFactor + (1 - dampingFactor) * k.getValue());
                    }
                    return nextState;
                },
                (t1, t2, e) -> Math.abs(t1 - t2) <= e,
                epsilon
        );
    }

    public static <V, E> GraphState<V, Double> execute(Graph<V, E> g, double dampingFactor) {
        return execute(g, dampingFactor, Finals.DEFAULT_PAGERANK_PRECISION);
    }
}
