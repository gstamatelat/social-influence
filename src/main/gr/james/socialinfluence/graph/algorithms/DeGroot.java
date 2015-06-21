package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.collections.GraphState;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeGroot {
    public static GraphState execute(Graph g, GraphState initialOpinions, double epsilon, boolean keepHistory) {
        HashSet<GraphState> stateHistory = new HashSet<GraphState>();
        GraphState lastState = initialOpinions;
        stateHistory.add(initialOpinions);

        Map<Vertex, Set<Edge>> outEdgesMap = g.getOutEdges();

        boolean stabilized = false;
        /*int reps = 0;*/
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
            if (keepHistory) {
                if (stateHistory.contains(nextState)) {
                    stabilized = true;
                    if (!nextState.equals(lastState)) {
                        Helper.logError(Finals.W_DEGROOT_PERIODIC, g.getMeta());
                    }
                }
                stateHistory.add(lastState = nextState);
            } else {
                lastState = nextState;
            }
            /*reps++;
            if (reps > 10000) {
                Set<Vertex> stubborn = g.getStubbornVertices();
                Map<Vertex, Set<Edge>> stubbornEdges = new HashMap<Vertex, Set<Edge>>();
                for (Vertex v : stubborn) {
                    stubbornEdges.put(v, v.getInEdges());
                }
                double avg = lastState.getMean();
                try {
                    g.exportToDot(new FileOutputStream("C:\\Users\\James\\Desktop\\graph.dot"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int y = 0;
            }*/
        }

        return lastState;
    }

    public static GraphState execute(Graph g, GraphState initialOpinions, double epsilon) {
        return execute(g, initialOpinions, epsilon, Finals.DEFAULT_HISTORY);
    }

    public static GraphState execute(Graph g, GraphState initialOpinions, boolean keepHistory) {
        return execute(g, initialOpinions, Finals.DEFAULT_EPSILON, keepHistory);
    }

    public static GraphState execute(Graph g, GraphState initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_EPSILON, Finals.DEFAULT_HISTORY);
    }
}