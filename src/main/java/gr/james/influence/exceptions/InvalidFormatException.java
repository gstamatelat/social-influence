package gr.james.influence.exceptions;

/**
 * An {@code InvalidFormatException} indicates that the format of a file is not valid.
 * <p>
 * Typically, this exception is raised when importing graphs from the filesystem.
 */
public class InvalidFormatException extends RuntimeException {
    /**
     * Constructs a new {@link InvalidFormatException}.
     */
    public InvalidFormatException() {
        super();
    }

    /**
     * Constructs a new {@link InvalidFormatException} with the specified detail message.
     *
     * @param messagePattern the error message text pattern
     * @param arguments      arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public InvalidFormatException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
