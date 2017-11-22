package gr.james.influence.util.exceptions;

/**
 * An {@code InvalidVertexException} indicates that a vertex argument is not in the graph imposed by the context.
 */
public class InvalidVertexException extends RuntimeException {
    /**
     * Constructs a new {@link InvalidVertexException}.
     */
    public InvalidVertexException() {
        super();
    }

    /**
     * Constructs a new {@link InvalidVertexException} with the specified detail message.
     *
     * @param messagePattern the error message text pattern
     * @param arguments      arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public InvalidVertexException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
