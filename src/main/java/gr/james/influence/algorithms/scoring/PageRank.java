package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.scoring.util.IterativeAlgorithmHelper;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;

import java.util.HashMap;
import java.util.Map;

public class PageRank {
    public static GraphState<Double> execute(Graph g, double dampingFactor, double epsilon) {
        HashMap<Vertex, Double> outStrengths = new HashMap<>();
        for (Vertex v : g) {
            outStrengths.put(v, g.getOutStrength(v));
        }

        return IterativeAlgorithmHelper.execute(
                g,
                new GraphState<>(g, 1.0),
                oldState -> {
                    GraphState<Double> nextState = new GraphState<>(g, 0.0);
                    for (Vertex v : g) {
                        Map<Vertex, GraphEdge> inEdges = g.getInEdges(v);
                        for (Map.Entry<Vertex, GraphEdge> e : inEdges.entrySet()) {
                            nextState.put(v, nextState.get(v) +
                                    e.getValue().getWeight() * oldState.get(e.getKey()) / outStrengths.get(e.getKey()));
                        }
                    }
                    for (Map.Entry<Vertex, Double> k : nextState.entrySet()) {
                        k.setValue(dampingFactor + (1 - dampingFactor) * k.getValue());
                    }
                    return nextState;
                },
                (t1, t2, e) -> Math.abs(t1 - t2) <= e,
                epsilon
        );
    }

    public static GraphState<Double> execute(Graph g, double dampingFactor) {
        return execute(g, dampingFactor, Finals.DEFAULT_PAGERANK_PRECISION);
    }
}
