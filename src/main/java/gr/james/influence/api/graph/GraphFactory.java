package gr.james.influence.api.graph;

@FunctionalInterface
public interface GraphFactory<V, E> {
    Graph<V, E> createGraph();
}
