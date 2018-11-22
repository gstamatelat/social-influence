package gr.james.influence.exceptions;

/**
 * An {@code IllegalWeightException} indicates that a weight argument is not valid.
 *
 * @deprecated will be removed after 2019-05-22
 */
@Deprecated
public class IllegalWeightException extends RuntimeException {
    /**
     * Constructs a new {@link IllegalWeightException}.
     */
    public IllegalWeightException() {
        super();
    }

    /**
     * Constructs a new {@link IllegalWeightException} with the specified detail message.
     *
     * @param messagePattern the error message text pattern
     * @param arguments      arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public IllegalWeightException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
