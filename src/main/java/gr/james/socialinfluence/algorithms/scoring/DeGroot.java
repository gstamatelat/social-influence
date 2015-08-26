package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.algorithms.scoring.util.IterativeAlgorithmHelper;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.collections.GraphState;

import java.util.Map;

public class DeGroot {
    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon) {
        return IterativeAlgorithmHelper.execute(
                g,
                initialOpinions,
                oldState -> {
                    GraphState<Double> nextState = new GraphState<>();
                    for (Vertex v : g) {
                        double vNewValue = 0.0;
                        for (Map.Entry<Vertex, Edge> e : g.getOutEdges(v).entrySet()) {
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

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION);
    }
}
