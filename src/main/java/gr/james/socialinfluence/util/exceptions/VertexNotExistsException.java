package gr.james.socialinfluence.util.exceptions;

public class VertexNotExistsException extends GraphException {
    /**
     * <p>Constructs a new {@code VertexNotExistsException}.</p>
     */
    public VertexNotExistsException() {
        super();
    }

    /**
     * <p>Constructs a new {@code VertexNotExistsException} with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see java.lang.String#format(String, Object...)
     */
    public VertexNotExistsException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
