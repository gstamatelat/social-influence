package gr.james.socialinfluence.util.exceptions;

public class InvalidVertexException extends GraphException {
    /**
     * <p>Constructs a new {@code InvalidVertexException}.</p>
     */
    public InvalidVertexException() {
        super();
    }

    /**
     * <p>Constructs a new {@code InvalidVertexException} with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public InvalidVertexException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
