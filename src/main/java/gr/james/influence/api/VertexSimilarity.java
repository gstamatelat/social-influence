package gr.james.influence.api;

@FunctionalInterface
public interface VertexSimilarity<V, T> {
    T similarity(V v1, V v2);
}
