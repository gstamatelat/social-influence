package gr.james.socialinfluence.game;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.ImmutableGraph;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;
import gr.james.socialinfluence.util.exceptions.GraphException;
import org.slf4j.Logger;

import java.util.*;

public abstract class Player {
    protected static final Logger log = Finals.LOG;

    private final Object lock = new Object();
    private Map<String, String> options = new HashMap<>();
    private boolean interrupted = false;

    /**
     * <p>Constructs a {@code Player} with default options.</p>
     */
    public Player() {
        options.putAll(defaultOptions());
    }

    /**
     * <p>Tests whether the player has been interrupted. The game mechanism requests player interruption when the
     * player has exhausted the available time for execution. When this flag is set to {@code true}, further moves
     * submitted by the player will be ignored and this is an indication that the player must terminate gracefully.
     * This flag is an alternative way of time tracking besides {@link GameDefinition#getExecution() getExecution()}
     * method. The interrupted status of the player is cleared by this method. In other words, if this method were to be
     * called twice in succession, the second call would return {@code false} (unless the current player were
     * interrupted again, after the first call had cleared its interrupted status and before the second call had
     * examined it). This method is also called after the player has finished processing, in order to clear the
     * interrupt flag.</p>
     *
     * @return {@code true} if the player was interrupted and must terminate, otherwise {@code false}
     */
    protected final boolean isInterrupted() {
        synchronized (lock) {
            boolean last_interrupt = this.interrupted;
            this.interrupted = false;
            return last_interrupt;
        }
    }

    /**
     * <p>Sets the interrupt flag to {@code true}. The player needs to invoke {@link #isInterrupted()} to access this
     * flag.</p>
     */
    public final void interrupt() {
        synchronized (lock) {
            this.interrupted = true;
        }
    }

    protected abstract void suggestMove(Graph g, GameDefinition d, MovePointer movePtr);

    /**
     * <p>Invokes {@link #suggestMove(Graph, GameDefinition, MovePointer)} on a separate thread.</p>
     *
     * @param g The {@link Graph} object that will be passed in {@code suggestMove()} as {@link ImmutableGraph}
     * @param d The {@link GameDefinition} object that will be passed directly in {@code suggestMove()}
     * @return the last move that was submitted in time from the player
     */
    public final Move getMove(Graph g, GameDefinition d) {
        final Graph finalGraph = ImmutableGraph.decorate(g);
        final MovePointer movePtr = new MovePointer();

        List<Throwable> thrown = new ArrayList<>();

        Thread t = new Thread(() -> suggestMove(finalGraph, d, movePtr));
        t.setUncaughtExceptionHandler((t1, e) -> thrown.add(e));

        Move m;

        try {
            t.start();
            t.join(d.getExecution());
            this.interrupt();
            m = movePtr.recall();
            int count = 0;
            while (t.isAlive()) {
                if (count > 0) {
                    log.warn(Finals.L_PLAYER_WAITING, count * d.getExecution() / 1000, this.getClass().getSimpleName());
                }
                t.join(d.getExecution());
                count++;
            }
        } catch (InterruptedException e) {
            throw Helper.convertCheckedException(e);
        }

        this.isInterrupted(); // This is called to clear the interrupt flag

        for (Throwable e : thrown) {
            if ((e instanceof Error) && Helper.isAssertionEnabled()) {
                throw new AssertionError(e);
            } else {
                Finals.LOG.warn(Finals.L_PLAYER_EXCEPTION, this.getClass().getSimpleName(), finalGraph, d,
                        Helper.getExceptionString(e));
            }
        }

        return m;
    }

    protected Map<String, String> defaultOptions() {
        return Collections.<String, String>emptyMap();
    }

    public final String getOption(String name) {
        Conditions.requireNonNull(name);
        if (!this.options.containsKey(name)) {
            throw new GraphException(Finals.E_PLAYER_NO_PARAMETER, this.getClass().getSimpleName(), name);
        }
        return this.options.get(name);
    }

    public final Player setOption(String name, String value) {
        name = Conditions.requireNonNull(name);
        value = Conditions.requireNonNull(value);
        if (!this.options.containsKey(name)) {
            throw new GraphException(Finals.E_PLAYER_NO_PARAMETER, this.getClass().getSimpleName(), name);
        } else {
            this.options.put(name, value);
        }
        return this;
    }

    public final Map<String, String> getOptions() {
        return Collections.unmodifiableMap(this.options);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
