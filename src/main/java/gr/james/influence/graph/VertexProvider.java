package gr.james.influence.graph;

@FunctionalInterface
public interface VertexProvider<V> {
    V getVertex();
}
