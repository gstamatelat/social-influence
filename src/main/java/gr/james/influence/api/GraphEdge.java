package gr.james.influence.api;

public interface GraphEdge<V, E> {
    E getEdge();

    V getSource();

    V getTarget();

    double getWeight();
}
