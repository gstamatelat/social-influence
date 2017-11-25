package gr.james.influence.util;

import gr.james.influence.api.Graph;
import gr.james.influence.exceptions.InvalidVertexException;

public final class Conditions {
    public static void requireArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <V, E> V requireNonNullAndExists(V v, Graph<V, E> g) {
        Conditions.requireNonNull(v);
        if (!g.containsVertex(v)) {
            throw new InvalidVertexException();
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
