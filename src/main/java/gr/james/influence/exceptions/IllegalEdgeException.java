package gr.james.influence.exceptions;

/**
 * An {@code IllegalEdgeException} indicates that an edge or vertex pair argument is not in the graph imposed by the
 * context.
 */
public class IllegalEdgeException extends RuntimeException {
    /**
     * Constructs a new {@link IllegalEdgeException}.
     */
    public IllegalEdgeException() {
        super();
    }

    /**
     * Constructs a new {@link IllegalEdgeException} with the specified detail message.
     *
     * @param messagePattern the error message text pattern
     * @param arguments      arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public IllegalEdgeException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
