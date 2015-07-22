package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.collections.states.DoubleGraphState;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PageRank {
    public static GraphState<Double> execute(Graph g, double dampingFactor, double epsilon) {
        Set<GraphState<Double>> stateHistory = new HashSet<>();
        GraphState<Double> lastState = new DoubleGraphState(g, 1.0);
        stateHistory.add(lastState);

        HashMap<Vertex, Double> outWeightSums = new HashMap<>();
        for (Vertex v : g.getVertices()) {
            outWeightSums.put(v, Helper.getWeightSum(g.getOutEdges(v).values()));
        }

        boolean stabilized = false;
        while (!stabilized) {
            GraphState<Double> nextState = new DoubleGraphState(g, 0.0);
            for (Vertex v : g.getVertices()) {
                Map<Vertex, Edge> inEdges = g.getInEdges(v);
                for (Map.Entry<Vertex, Edge> e : inEdges.entrySet()) {
                    nextState.put(v, nextState.get(v) + lastState.get(e.getKey()) / outWeightSums.get(e.getKey()));
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
