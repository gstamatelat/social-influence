package gr.james.influence.util.exceptions;

@Deprecated
public class WeightException extends GraphException {
    /**
     * <p>Constructs a new {@code WeightException}.</p>
     */
    public WeightException() {
        super();
    }

    /**
     * <p>Constructs a new {@code WeightException} with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public WeightException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
