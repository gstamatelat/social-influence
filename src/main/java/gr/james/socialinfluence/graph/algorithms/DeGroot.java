package gr.james.socialinfluence.graph.algorithms;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.collections.states.DoubleGraphState;
import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

import java.util.HashSet;
import java.util.Map;

public class DeGroot {
    public static DoubleGraphState execute(Graph g, DoubleGraphState initialOpinions, double epsilon, boolean keepHistory) {
        HashSet<DoubleGraphState> stateHistory = new HashSet<>();
        DoubleGraphState lastState = initialOpinions;
        stateHistory.add(initialOpinions);

        boolean stabilized = false;
        /*int reps = 0;*/
        while (!stabilized) {
            DoubleGraphState nextState = new DoubleGraphState(g, 0.0);

            for (Vertex v : g.getVertices()) {
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
                        Finals.LOG.warn(Finals.L_DEGROOT_PERIODIC, g.toString());
                    }
                }
                stateHistory.add(lastState = nextState);
            } else {
                lastState = nextState;
            }
            /*reps++;
            if (reps > 50000) {
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

    public static DoubleGraphState execute(Graph g, DoubleGraphState initialOpinions, double epsilon) {
        return execute(g, initialOpinions, epsilon, Finals.DEFAULT_DEGROOT_HISTORY);
    }

    public static DoubleGraphState execute(Graph g, DoubleGraphState initialOpinions, boolean keepHistory) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION, keepHistory);
    }

    public static DoubleGraphState execute(Graph g, DoubleGraphState initialOpinions) {
        return execute(g, initialOpinions, Finals.DEFAULT_DEGROOT_PRECISION, Finals.DEFAULT_DEGROOT_HISTORY);
    }
}
