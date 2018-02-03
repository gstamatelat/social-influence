package gr.james.influence.algorithms.layout;

import gr.james.influence.graph.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.collections.GraphState;

import java.util.HashSet;
import java.util.Set;

public class ClusteringCoefficient {
    public static <V, E> GraphState<V, Double> localClusteringCoefficient(Graph<V, E> g) {
        GraphState<V, Double> cc = GraphState.create();
        for (V v : g) {
            Set<V> neighborhood = new HashSet<>();
            neighborhood.addAll(g.adjacentOut(v));
            neighborhood.addAll(g.adjacentIn(v));

            Graph<V, E> nGraph = Graphs.deepCopy(g, neighborhood);
            cc.put(v, Graphs.getDensity(nGraph));
        }
        return cc;
    }

    public static <V, E> double globalClusteringCoefficient(Graph<V, E> g, GraphState<V, Double> localClusteringCoefficient) {
        throw new UnsupportedOperationException();
    }
}
