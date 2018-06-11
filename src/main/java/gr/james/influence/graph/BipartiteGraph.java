package gr.james.influence.graph;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.Set;

/**
 * Represents a bipartite graph, a graph whose vertices can be divided into two disjoint and independent sets A and B
 * such that each edge connects a vertex in A to one in B.
 * <p>
 * A {@code BipartiteGraph} is implemented as an {@link UndirectedGraph} which also contains the two vertex sets A and
 * B. The {@code BipartiteGraph} class guarantees, via exceptions, that there won't be any edges between two vertices of
 * the same disjoint set.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
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
    default BipartiteGraph<V, E> copy() {
        return create(this);
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
        return copy().asUnmodifiable();
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

    /**
     * Returns the disjoint set of this bipartite graph that contains the specified vertex.
     * <p>
     * More specifically, this method will return {@code vertexSetA()} if {@code v} is in set {@code A} and
     * {@code vertexSetB()} if {@code v} is in set {@code B}.
     *
     * @param v the vertex
     * @return the disjoint set of this bipartite graph that contains {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not an element of this graph
     */
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

    /**
     * Returns the disjoint set of this bipartite graph that does not contain the specified vertex.
     * <p>
     * More specifically, this method will return {@code vertexSetB()} if {@code v} is in set {@code A} and
     * {@code vertexSetA()} if {@code v} is in set {@code B}.
     *
     * @param v the vertex
     * @return the disjoint set of this bipartite graph that does not contain {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not an element of this graph
     */
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
