package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.states.DoubleGraphState;

import java.util.Map;

public class DeGroot {
    public static final int DEFAULT_HISTORY = Integer.MAX_VALUE;

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon, int history) {
        IterativeAlgorithm a = oldState -> {
            GraphState<Double> nextState = new DoubleGraphState(g, 0.0);
            for (Vertex v : g) {
                double vNewValue = 0.0;
                for (Map.Entry<Vertex, Edge> e : g.getOutEdges(v).entrySet()) {
                    vNewValue = vNewValue + (
                            e.getValue().getWeight() * oldState.get(e.getKey())
                    );
                }
                nextState.put(v, vNewValue / Helper.getWeightSum(g.getOutEdges(v).values()));
            }
            return nextState;
        };

        return a.execute(g, initialOpinions, epsilon, history);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon) {
        return execute(g, initialOpinions, epsilon, DEFAULT_HISTORY);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, int history) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION, history);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION, DEFAULT_HISTORY);
    }
}
