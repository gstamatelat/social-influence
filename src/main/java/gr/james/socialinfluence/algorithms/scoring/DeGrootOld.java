package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.states.DoubleGraphState;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Deprecated
public class DeGrootOld {
    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon, boolean keepHistory) {
        Set<GraphState<Double>> stateHistory = new HashSet<>();
        GraphState<Double> lastState = initialOpinions;
        stateHistory.add(initialOpinions);

        boolean stabilized = false;
        while (!stabilized) {
            GraphState<Double> nextState = new DoubleGraphState(g, 0.0);

            for (Vertex v : g) {
                double vNewValue = 0.0;
                for (Map.Entry<Vertex, Edge> e : g.getOutEdges(v).entrySet()) {
                    vNewValue = vNewValue + (
                            e.getValue().getWeight() * lastState.get(e.getKey())
                    );
                }
                nextState.put(v, vNewValue / Helper.getWeightSum(g.getOutEdges(v).values()));
            }

            if (nextState.subtract(lastState).abs().lessThan(epsilon)) {
                stabilized = true;
            }
            if (keepHistory) {
                if (stateHistory.contains(nextState)) {
                    stabilized = true;
                    if (!nextState.equals(lastState)) {
                        Finals.LOG.warn(Finals.L_DEGROOT_PERIODIC, g);
                    }
                }
                stateHistory.add(lastState = nextState);
            } else {
                lastState = nextState;
            }
        }

        return lastState;
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, double epsilon) {
        return execute(g, initialOpinions, epsilon, Finals.DEFAULT_DEGROOT_HISTORY);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions, boolean keepHistory) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION, keepHistory);
    }

    public static GraphState<Double> execute(Graph g, GraphState<Double> initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION, Finals.DEFAULT_DEGROOT_HISTORY);
    }
}
