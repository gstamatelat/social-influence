package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.collections.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeGroot {
    public static GraphState execute(Graph g, GraphState initialOpinions, double epsilon) {
        HashSet<GraphState> stateHistory = new HashSet<GraphState>();
        GraphState lastState = initialOpinions;
        stateHistory.add(initialOpinions);

        Map<Vertex, Set<Edge>> outEdgesMap = g.getOutEdges();

        boolean stabilized = false;
        while (!stabilized) {
            GraphState nextState = new GraphState(g, 0.0);

            for (Vertex v : outEdgesMap.keySet()) {
                double vNewValue = 0.0;
                for (Edge e : outEdgesMap.get(v)) {
                    vNewValue = vNewValue + (
                            e.getWeight() * lastState.get(e.getTarget())
                    );
                }
                nextState.put(v, vNewValue / Helper.getWeightSum(outEdgesMap.get(v)));
            }

            if (nextState.subtractAbs(lastState).lessThan(epsilon)) {
                stabilized = true;
            }
            if (stateHistory.contains(nextState)) {
                stabilized = true;
                if (!nextState.equals(lastState)) {
                    //Helper.log("Periodic");
                }
            }
            stateHistory.add(lastState = nextState);
        }

        return lastState;
    }

    public static GraphState execute(Graph g, GraphState initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_EPSILON);
    }
}