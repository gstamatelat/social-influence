package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

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

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default BipartiteGraph<V, E> asUnmodifiable() {
        if (this instanceof UnmodifiableBipartiteGraph) {
            return this;
        }
        return new UnmodifiableBipartiteGraph<>(this);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default BipartiteGraph<V, E> toImmutable() {
        return create(this).asUnmodifiable();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    default BipartiteGraph<V, E> subGraph(Set<V> vertices) {
        return new BipartiteSubGraph<>(this, vertices);
    }

    default Set<V> setOf(V v) {
        Conditions.requireNonNull(v);
        final boolean inA = vertexSetA().contains(v);
        final boolean inB = vertexSetB().contains(v);
        assert !(inA && inB);
        if (!inA && !inB) {
            throw new IllegalVertexException();
        }
        if (inA) {
            return vertexSetA();
        } else {
            return vertexSetB();
        }
    }

    default Set<V> otherSetOf(V v) {
        Conditions.requireNonNull(v);
        final boolean inA = vertexSetA().contains(v);
        final boolean inB = vertexSetB().contains(v);
        assert !(inA && inB);
        if (!inA && !inB) {
            throw new IllegalVertexException();
        }
        if (inA) {
            return vertexSetB();
        } else {
            return vertexSetA();
        }
    }

    default BipartiteGraph<V, E> asSwapped() {
        return new SwappedBipartiteGraph<>(this);
    }

    Set<V> vertexSetA();

    Set<V> vertexSetB();

    boolean addVertexInA(V v);

    boolean addVertexInB(V v);
}
