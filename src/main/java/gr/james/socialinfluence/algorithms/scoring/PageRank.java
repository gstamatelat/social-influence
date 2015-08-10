package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.states.DoubleGraphState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PageRank {
    public static GraphState<Double> execute(Graph g, double dampingFactor, double epsilon) {
        Set<GraphState<Double>> stateHistory = new HashSet<>();
        GraphState<Double> lastState = new DoubleGraphState(g, 1.0);
        stateHistory.add(lastState);

        /* Using this weight map, method is becoming a little faster */
        HashMap<Vertex, Double> outWeightSums = new HashMap<>();
        for (Vertex v : g) {
            outWeightSums.put(v, Helper.getWeightSum(g.getOutEdges(v).values()));
        }

        boolean stabilized = false;
        while (!stabilized) {
            GraphState<Double> nextState = new DoubleGraphState(g, 0.0);
            for (Vertex v : g) {
                Map<Vertex, Edge> inEdges = g.getInEdges(v);
                for (Map.Entry<Vertex, Edge> e : inEdges.entrySet()) {
                    nextState.put(v, nextState.get(v) +
                            e.getValue().getWeight() * lastState.get(e.getKey()) / outWeightSums.get(e.getKey()));
                }
            }

            for (Map.Entry<Vertex, Double> k : nextState.entrySet()) {
                k.setValue(dampingFactor + (1 - dampingFactor) * k.getValue());
            }

            if (nextState.subtract(lastState).abs().lessThan(epsilon)) {
                stabilized = true;
            }
            if (stateHistory.contains(nextState)) {
                stabilized = true;
                /*if (!nextState.equals(lastState)) {
                    int period = stateHistory.size() - stateHistory.indexOf(nextState);
                    return null;
                }*/
            }
            stateHistory.add(lastState = nextState);
        }

        return lastState;
    }

    public static GraphState<Double> execute(Graph g, double dampingFactor) {
        return execute(g, dampingFactor, Finals.DEFAULT_PAGERANK_PRECISION);
    }
}
