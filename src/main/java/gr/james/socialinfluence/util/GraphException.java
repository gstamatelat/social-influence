package gr.james.socialinfluence.util;

public class GraphException extends RuntimeException {
    /**
     * <p>Constructs a new graph exception.</p>
     */
    public GraphException() {
        super();
    }

    /**
     * <p>Constructs a new graph exception with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see java.lang.String#format(String, Object...)
     */
    public GraphException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }

    /**
     * <p>Constructs a new graph exception with the specified cause.</p>
     *
     * @param cause the cause
     */
    public GraphException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructs a new graph exception with the specified detail message and cause.</p>
     *
     * @param msg   the detail message.
     * @param cause the cause
     */
    public GraphException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
