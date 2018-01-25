package gr.james.influence.api.graph;

public interface DirectedEdge<V, E> {
    E edge();

    V source();

    V target();

    double weight();
}
