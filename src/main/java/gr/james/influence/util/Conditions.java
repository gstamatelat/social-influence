package gr.james.influence.util;

import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;

public final class Conditions {
    public static void requireArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void requireArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Validates that a graph {@code g} and a vertex {@code v} are not {@code null} and that {@code v} is in {@code g}.
     * <p>
     * More specifically, performs the following checks in order:
     * <ol>
     * <li>{@code g != null} ({@link NullPointerException})</li>
     * <li>{@code v != null} ({@link NullPointerException})</li>
     * <li>{@code g.containsVertex(v)} ({@link IllegalVertexException})</li>
     * </ol>
     *
     * @param g   the graph
     * @param v   the vertex
     * @param <V> the vertex type
     * @return {@code v}
     * @throws NullPointerException   if {@code g} or {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in {@code g}
     */
    public static <V> V requireVertexInGraph(DirectedGraph<V, ?> g, V v) {
        if (!g.containsVertex(v)) {
            throw new IllegalVertexException();
        }
        return v;
    }

    public static <T> T requireNonNull(T o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o;
    }

    public static <T> T requireNonNull(T o, String errorMessageTemplate, Object... errorMessageArgs) {
        if (o == null) {
            throw new NullPointerException(String.format(errorMessageTemplate, errorMessageArgs));
        }
        return o;
    }

    public static void requireAllNonNull(Object... t) {
        for (Object o : t) {
            requireNonNull(o);
        }
    }

    public static void assertion(boolean expression) {
        if (!expression) {
            throw new AssertionError();
        }
    }
}
