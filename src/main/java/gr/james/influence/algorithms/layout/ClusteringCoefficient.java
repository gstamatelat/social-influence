package gr.james.influence.algorithms.layout;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.collections.GraphState;

import java.util.HashSet;
import java.util.Set;

public class ClusteringCoefficient {
    public static <V, E> GraphState<V, Double> localClusteringCoefficient(Graph<V, E> g) {
        GraphState<V, Double> cc = new GraphState<>();
        for (V v : g) {
            Set<V> neighborhood = new HashSet<>();
            neighborhood.addAll(g.getOutEdges(v).keySet());
            neighborhood.addAll(g.getInEdges(v).keySet());

            Graph<V, E> nGraph = Graphs.deepCopy(g, g.getGraphFactory(), neighborhood);
            cc.put(v, nGraph.getDensity());
        }
        return cc;
    }

    public static <V, E> double globalClusteringCoefficient(Graph<V, E> g, GraphState<V, Double> localClusteringCoefficient) {
        // TODO: Implement as weighted average of the local clustering coefficient
        throw new UnsupportedOperationException();
    }
}
