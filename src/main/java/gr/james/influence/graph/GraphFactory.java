package gr.james.influence.graph;

@FunctionalInterface
public interface GraphFactory<V, E> {
    Graph<V, E> createGraph();
}
