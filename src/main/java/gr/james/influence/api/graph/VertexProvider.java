package gr.james.influence.api.graph;

@FunctionalInterface
public interface VertexProvider<V> {
    V getVertex();
}
