package gr.james.influence.util.exceptions;

/**
 * An {@code InvalidVertexException} usually indicates
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
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public InvalidVertexException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
