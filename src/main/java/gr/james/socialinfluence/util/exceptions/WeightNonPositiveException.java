package gr.james.socialinfluence.util.exceptions;

public class WeightNonPositiveException extends GraphException {
    /**
     * <p>Constructs a new {@code WeightNonPositiveException}.</p>
     */
    public WeightNonPositiveException() {
        super();
    }

    /**
     * <p>Constructs a new {@code WeightNonPositiveException} with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public WeightNonPositiveException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
