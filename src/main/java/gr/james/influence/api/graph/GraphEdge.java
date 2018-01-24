package gr.james.influence.api.graph;

public interface GraphEdge<V, E> {
    E getEdge();

    V getSource();

    V getTarget();

    double getWeight();
}
