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

    // TODO: This method creates an ambiguous call in combination with the above
    /*public static void requireNonNull(Object... t) {
        for (Object o : t) {
            requireNonNull(o);
        }
    }*/
}
