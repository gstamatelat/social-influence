package gr.james.influence.graph;

import java.util.Set;

public interface BipartiteGraph<V, E> extends UndirectedGraph<V, E> {
    static <V, E> BipartiteGraph<V, E> create() {
        return new BipartiteGraphImpl<>();
    }

    static <V, E> BipartiteGraph<V, E> create(int expectedVertexCount) {
        return new BipartiteGraphImpl<>(expectedVertexCount);
    }

    static <V, E> BipartiteGraph<V, E> create(BipartiteGraph<V, E> g) {
        return new BipartiteGraphImpl<>(g);
    }

    Set<V> vertexASet();

    Set<V> vertexBSet();

    boolean addVertexInA(V v);

    boolean addVertexInB(V v);
}
