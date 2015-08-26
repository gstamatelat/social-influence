package gr.james.influence.game;

import gr.james.influence.util.Conditions;
import gr.james.influence.util.Finals;

/**
 * <p>Simple thread-safe wrapper class to avoid illegal calls on a {@link gr.james.influence.game.Move} object
 * from the engine and player threads. All methods of this class are thread-safe. An object of type {@code MovePointer}
 * cannot contain a null {@link Move}.</p>
 */
public class MovePointer {
    private final Object lock = new Object();
    private Move ref;

    public MovePointer() {
        ref = new Move();
    }

    /**
     * <p>Gets a copy of the internal {@link Move} object of this instance.</p>
     * <p><b>Complexity:</b> O(n)</p>
     *
     * @return a deep copy of the internal {@link Move} object
     */
    public Move recall() {
        synchronized (lock) {
            return ref.deepCopy();
        }
    }

    /**
     * <p>Sets the internal {@link Move} object of this instance.</p>
     * <p><b>Complexity:</b> O(n)</p>
     *
     * @param e the {@link Move} to submit. This input will be deep-copied before it is submit as part of the
     *          {@link MovePointer} thread-safety.
     * @throws NullPointerException when the input move is {@code null}
     */
    public void submit(Move e) {
        synchronized (lock) {
            this.ref = Conditions.requireNonNull(e, Finals.E_MOVEPOINTER_SET_NULL).deepCopy();
        }
    }
}
