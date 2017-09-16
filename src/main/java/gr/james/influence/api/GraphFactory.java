package gr.james.influence.api;

@FunctionalInterface
public interface GraphFactory<V, E> {
    Graph<V, E> createGraph();
}
