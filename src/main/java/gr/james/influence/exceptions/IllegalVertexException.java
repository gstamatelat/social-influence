package gr.james.influence.exceptions;

/**
 * An {@code IllegalVertexException} indicates that a vertex argument is not in the graph imposed by the context.
 */
public class IllegalVertexException extends RuntimeException {
    /**
     * Constructs a new {@link IllegalVertexException}.
     */
    public IllegalVertexException() {
        super();
    }

    /**
     * Constructs a new {@link IllegalVertexException} with the specified detail message.
     *
     * @param messagePattern the error message text pattern
     * @param arguments      arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public IllegalVertexException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
