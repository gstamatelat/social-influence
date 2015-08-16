package gr.james.socialinfluence.util.exceptions;

public class TournamentException extends GraphException {
    /**
     * <p>Constructs a new {@code TournamentException}.</p>
     */
    public TournamentException() {
        super();
    }

    /**
     * <p>Constructs a new {@code TournamentException} with the specified detail message.</p>
     *
     * @param messagePattern The error message text pattern
     * @param arguments      Arguments referenced by the format specifiers in the format string
     * @see String#format(String, Object...)
     */
    public TournamentException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
    }
}
