package gr.james.socialinfluence.util.exceptions;

public class GraphException extends RuntimeException {
    /**
     * <p>Constructs a new {@code GraphException}.</p>
     */
    public GraphException() {
        super();
    }

    /**
     * <p>Constructs a new {@code GraphException} with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see java.lang.String#format(String, Object...)
     */
    public GraphException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
