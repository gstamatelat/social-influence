package gr.james.socialinfluence.util;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.exceptions.InvalidVertexException;

public final class Conditions {
    public static void requireArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static Vertex requireNonNullAndExists(Vertex v, Graph g) {
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
