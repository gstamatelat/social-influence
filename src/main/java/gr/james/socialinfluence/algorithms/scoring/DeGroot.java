package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.collections.GraphState;

import java.util.Map;

public class DeGroot {
    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon, IterativeAlgorithmHandler handler) {
        IterativeAlgorithm a = oldState -> {
            GraphState<Double> nextState = new GraphState<>(g, 0.0);
            for (Vertex v : g) {
                double vNewValue = 0.0;
                for (Map.Entry<Vertex, Edge> e : g.getOutEdges(v).entrySet()) {
                    vNewValue = vNewValue + (
                            e.getValue().getWeight() * oldState.get(e.getKey())
                    );
                }
                nextState.put(v, vNewValue / Helper.getWeightSum(g.getOutEdges(v).values()));
            }
            if (handler != null) {
                handler.newState(oldState, nextState);
            }
            return nextState;
        };

        return a.execute(g, initialOpinions, epsilon);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon) {
        return execute(g, initialOpinions, epsilon, null);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION);
    }
}
