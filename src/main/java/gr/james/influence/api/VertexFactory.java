package gr.james.influence.api;

public interface VertexFactory<V> {
    V createVertex();

    V createVertex(String s);
}
