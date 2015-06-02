package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PageRank {
    public static GraphState execute(Graph g, double dampingFactor, double epsilon) {
        HashSet<GraphState> stateHistory = new HashSet<GraphState>();
        GraphState lastState = new GraphState(g, 1.0);
        stateHistory.add(lastState);

        HashMap<Vertex, Double> outWeightSums = new HashMap<Vertex, Double>();
        for (Vertex v : g.getVertices()) {
            outWeightSums.put(v, 0.0);
        }
        for (Edge e : g.getEdges()) {
            outWeightSums.put(e.getSource(), outWeightSums.get(e.getSource()) + e.getWeight());
        }

        boolean stabilized = false;
        while (!stabilized) {
            GraphState nextState = new GraphState(g, 0.0);
            for (Edge e : g.getEdges()) {
                nextState.put(e.getTarget(),
                        nextState.get(e.getTarget()) + (
                                e.getWeight() * lastState.get(e.getSource()) / outWeightSums.get(e.getSource())
                        )
                );
            }
            for (Map.Entry<Vertex, Double> k : nextState.entrySet()) {
                k.setValue(dampingFactor + (1 - dampingFactor) * k.getValue());
            }

            if (nextState.subtractAbs(lastState).lessThan(epsilon)) {
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

    public static GraphState execute(Graph g, double dampingFactor) {
        return execute(g, dampingFactor, Finals.DEFAULT_EPSILON);
    }
}