package gr.james.influence.api;

@FunctionalInterface
public interface VertexProvider<V> {
    V get();
}
