package gr.james.socialinfluence.helper;

public class GraphException extends RuntimeException {
    /**
     * Constructs a new graph exception.
     */
    public GraphException() {
        super();
    }

    /**
     * Constructs a new graph exception with the specified detail message.
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see java.lang.String#format(String, Object...)
     */
    public GraphException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }

    /**
     * Constructs a new graph exception with the specified cause.
     *
     * @param cause the cause
     */
    public GraphException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new graph exception with the specified detail message and cause.
     *
     * @param msg   the detail message.
     * @param cause the cause
     */
    public GraphException(String msg, Throwable cause) {
        super(msg, cause);
    }
}