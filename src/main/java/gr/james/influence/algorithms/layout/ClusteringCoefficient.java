package gr.james.influence.algorithms.layout;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;

import java.util.HashSet;
import java.util.Set;

public class ClusteringCoefficient {
    public static GraphState<Double> localClusteringCoefficient(Graph g) {
        GraphState<Double> cc = new GraphState<>();
        for (Vertex v : g) {
            Set<Vertex> neighborhood = new HashSet<>();
            neighborhood.addAll(g.getOutEdges(v).keySet());
            neighborhood.addAll(g.getInEdges(v).keySet());

            Graph nGraph = GraphUtils.deepCopy(g, neighborhood);
            cc.put(v, nGraph.getDensity());
        }
        return cc;
    }

    public static double globalClusteringCoefficient(Graph g, GraphState<Double> localClusteringCoefficient) {
        // TODO: Implement as weighted average of the local clustering coefficient
        throw new UnsupportedOperationException();
    }
}
