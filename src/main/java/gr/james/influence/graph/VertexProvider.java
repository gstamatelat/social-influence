package gr.james.influence.graph;

import gr.james.influence.algorithms.generators.GraphGenerator;
import gr.james.influence.util.Conditions;

import java.math.BigInteger;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represents an entity that can produce vertices.
 * <p>
 * Normally, this interface is used on graph generators ({@link GraphGenerator}) because generators need to add generic
 * vertices to a graph.
 * <p>
 * A {@code VertexProvider} has the single method {@link #create()} which must create and return a new vertex. Each
 * instance of {@code VertexProvider} must return discrete vertices over its lifetime. More formally, the method
 * {@link #create()} cannot return a vertex that is equal to another one that has already been returned by the same
 * instance. The equality is based on the {@code V.equals} method. If no more discrete vertices can be produced, a
 * {@link java.util.NoSuchElementException} will be raised. Furthermore, this method cannot return {@code null}.
 *
 * @param <V> the vertex type
 */
@FunctionalInterface
public interface VertexProvider<V> {
    /**
     * Package-wide {@link VertexProvider} that returns all the non-negative {@link Integer} values in order (starting
     * from {@code 0}).
     * <p>
     * The {@link #create()} method will throw {@link NoSuchElementException} if all the possible non-negative integers
     * are exhausted.
     */
    VertexProvider<Integer> INTEGER_PROVIDER = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer create() throws NoSuchElementException {
            if (nextId < 0) {
                throw new NoSuchElementException();
            }
            return nextId++;
        }
    };

    /**
     * Package-wide {@link VertexProvider} that returns all the non-negative {@link Integer} values as {@link String} in
     * order (starting from {@code 0}).
     * <p>
     * The {@link #create()} method will never throw {@link NoSuchElementException} as it is internally based on a
     * {@link BigInteger} implementation.
     */
    VertexProvider<String> STRING_PROVIDER = new VertexProvider<String>() {
        private BigInteger nextId = BigInteger.ZERO;

        @Override
        public String create() {
            final BigInteger r = nextId;
            nextId = nextId.add(BigInteger.ONE);
            return r.toString();
        }
    };

    /**
     * Construct a {@link VertexProvider} that provides elements from a pool.
     * <p>
     * The provider returns the elements with the same order as the {@link Iterator} of {@code pool}. The
     * {@link #create()} method will throw {@link NoSuchElementException} when the elements in {@code pool} are
     * exhausted.
     * <p>
     * Because the resulting {@link VertexProvider} internally uses the {@link Iterator} of {@code pool}, if
     * {@code pool} is modified before the provider is exhausted, a {@link ConcurrentModificationException} will be
     * thrown.
     *
     * @param pool the pool of elements
     * @param <V>  the type of vertex
     * @return a {@link VertexProvider} that provides elements from {@code pool}
     * @throws NullPointerException if {@code pool} is {@code null}
     */
    static <V> VertexProvider<V> fromPool(Set<V> pool) {
        Conditions.requireNonNull(pool);
        return new VertexProvider<V>() {
            private final Iterator<V> it = pool.iterator();

            @Override
            public V create() throws NoSuchElementException {
                if (!it.hasNext()) {
                    throw new NoSuchElementException();
                }
                return it.next();
            }
        };
    }

    /**
     * Creates and returns a new discrete vertex.
     * <p>
     * This method cannot return {@code null} and also cannot return an object equal to a vertex that was returned
     * previously.
     *
     * @return a new discrete vertex
     * @throws NoSuchElementException if this {@link VertexProvider} cannot produce any more discrete vertices
     */
    V create() throws NoSuchElementException;
}
