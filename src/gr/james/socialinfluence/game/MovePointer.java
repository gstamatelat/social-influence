package gr.james.socialinfluence.game;

/**
 * <p>Simple thread-safe wrapper class to avoid illegal calls on a {@link gr.james.socialinfluence.game.Move} object
 * from the engine and player threads. All methods of this class are thread-safe.</p>
 */
public class MovePointer {
    private final Object lock = new Object();
    private Move ref;

    public MovePointer() {
        ref = null;
    }

    /**
     * <p>Gets a copy of the internal {@link Move} object of this instance.</p>
     * <p><b>Running Time:</b> ???</p>
     *
     * @return a deep copy of the internal {@link Move} object
     */
    public Move get() {
        synchronized (lock) {
            return ref.deepCopy();
        }
    }

    /**
     * <p>Sets the internal {@link Move} object of this instance.</p>
     * <p><b>Running Time:</b> ???</p>
     *
     * @param e the {@link Move} to set. This input will be deep-copied before it is set as part of the
     *          {@link MovePointer} thread-safety.
     */
    public void set(Move e) {
        synchronized (lock) {
            this.ref = e.deepCopy();
        }
    }
}