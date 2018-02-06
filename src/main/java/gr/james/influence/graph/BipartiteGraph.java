package gr.james.influence.graph;

import java.util.Set;

public interface BipartiteGraph<V, E> extends UndirectedGraph<V, E> {
    /**
     * Creates and returns a new empty {@link BipartiteGraph}.
     *
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return a new empty {@link BipartiteGraph}
     */
    static <V, E> BipartiteGraph<V, E> create() {
        return new BipartiteGraphImpl<>();
    }

    /**
     * Creates and returns a new empty {@link BipartiteGraph} with some expectation on the vertex count.
     *
     * @param expectedVertexCount the expected vertex count
     * @param <V>                 the vertex type
     * @param <E>                 the edge type
     * @return a new empty {@link BipartiteGraph}
     * @throws IllegalArgumentException if {@code expectedVertexCount} is negative
     */
    static <V, E> BipartiteGraph<V, E> create(int expectedVertexCount) {
        return new BipartiteGraphImpl<>(expectedVertexCount);
    }

    /**
     * Creates and returns a new {@link BipartiteGraph} from a copy of the given graph.
     *
     * @param g   the graph to copy
     * @param <V> the vertex type
     * @param <E> the edge type
     * @return a new {@link BipartiteGraph} from a copy of {@code g}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    static <V, E> BipartiteGraph<V, E> create(BipartiteGraph<V, E> g) {
        return new BipartiteGraphImpl<>(g);
    }

    @Override
    default BipartiteGraph<V, E> asUnmodifiable() {
        return new UnmodifiableBipartiteGraph<>(this);
    }

    @Override
    default BipartiteGraph<V, E> toImmutable() {
        return create(this).asUnmodifiable();
    }

    Set<V> vertexASet();

    Set<V> vertexBSet();

    boolean addVertexInA(V v);

    boolean addVertexInB(V v);
}
