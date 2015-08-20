package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.collections.GraphState;

import java.util.HashMap;
import java.util.Map;

public class PageRank {
    public static GraphState<Double> execute(Graph g, double dampingFactor, double epsilon, IterativeAlgorithmHandler handler) {
        /* Using this weight map, method is becoming a little faster */
        HashMap<Vertex, Double> outWeightSums = new HashMap<>();
        for (Vertex v : g) {
            outWeightSums.put(v, Helper.getWeightSum(g.getOutEdges(v).values()));
        }

        IterativeAlgorithm a = oldState -> {
            GraphState<Double> nextState = new GraphState<>(g, 0.0);
            for (Vertex v : g) {
                Map<Vertex, Edge> inEdges = g.getInEdges(v);
                for (Map.Entry<Vertex, Edge> e : inEdges.entrySet()) {
                    nextState.put(v, nextState.get(v) +
                            e.getValue().getWeight() * oldState.get(e.getKey()) / outWeightSums.get(e.getKey()));
                }
            }
            for (Map.Entry<Vertex, Double> k : nextState.entrySet()) {
                k.setValue(dampingFactor + (1 - dampingFactor) * k.getValue());
            }
            if (handler != null) {
                handler.newState(oldState, nextState);
            }
            return nextState;
        };

        return a.execute(g, new GraphState<>(g, 1.0), epsilon);
    }

    public static GraphState<Double> execute(Graph g, double dampingFactor, double epsilon) {
        return execute(g, dampingFactor, epsilon, null);
    }

    public static GraphState<Double> execute(Graph g, double dampingFactor) {
        return execute(g, dampingFactor, Finals.DEFAULT_PAGERANK_PRECISION);
    }
}
