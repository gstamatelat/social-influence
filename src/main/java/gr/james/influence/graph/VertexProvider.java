package gr.james.influence.graph;

import gr.james.influence.algorithms.generators.GraphGenerator;

import java.util.NoSuchElementException;

/**
 * Represents an entity that can produce vertices.
 * <p>
 * Normally, this interface is used on graph generators ({@link GraphGenerator}) because generators need to add generic
 * vertices to a graph.
 * <p>
 * A {@code VertexProvider} has the single method {@link #getVertex()} which must create and return a new vertex. Each
 * instance of {@code VertexProvider} must return discrete vertices over its lifetime. More formally, the method
 * {@link #getVertex()} cannot return a vertex that is equal to another one that has already been returned by the same
 * instance. The equality is based on the {@code V.equals} method. If no more discrete vertices can be produced, a
 * {@link java.util.NoSuchElementException} will be raised. Furthermore, this method cannot
 * return {@code null}.
 *
 * @param <V> the vertex type
 */
@FunctionalInterface
public interface VertexProvider<V> {
    /**
     * Package-wide {@link VertexProvider} that returns all the non-negative {@link Integer} values in order (starting
     * from {@code 0}).
     * <p>
     * The {@link #getVertex()} method will throw {@link NoSuchElementException} if all the possible non-negative
     * integers are exhausted.
     */
    VertexProvider<Integer> intProvider = new VertexProvider<Integer>() {
        private int nextId = 0;

        @Override
        public Integer getVertex() throws NoSuchElementException {
            if (nextId < 0) {
                throw new NoSuchElementException();
            }
            return nextId++;
        }
    };

    /**
     * Creates and returns a new discrete vertex.
     * <p>
     * This method cannot return {@code null} and also cannot return an object equal to a vertex that was returned
     * previously.
     *
     * @return a new discrete vertex
     * @throws NoSuchElementException if this {@link VertexProvider} cannot produce any more discrete vertices
     */
    V getVertex() throws NoSuchElementException;
}
