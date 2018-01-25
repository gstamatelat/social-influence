package gr.james.influence.api.graph;

public interface DirectedEdge<V, E> {
    E getEdge();

    V getSource();

    V getTarget();

    double getWeight();
}
