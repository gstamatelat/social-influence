package gr.james.socialinfluence.game;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.ImmutableGraph;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.exceptions.GraphException;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Player {
    protected static final Logger log = Finals.LOG;
    protected Map<String, String> options = new HashMap<>(); // TODO: Convert this field to private
    private boolean interrupted = false;

    /**
     * <p>Constructs a {@code Player} with default options.</p>
     */
    public Player() {
        putDefaultOptions();
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
        boolean last_interrupt = this.interrupted;
        this.interrupted = false;
        return last_interrupt;
    }

    /**
     * <p>Sets the interrupt flag to {@code true}. The player needs to invoke {@link #isInterrupted()} to access this
     * flag.</p>
     */
    public final void interrupt() {
        this.interrupted = true;
    }

    // TODO: Convert this to protected so it's not visible on mains etc (but then PlayerRunnable would complain)
    public abstract void suggestMove(ImmutableGraph g, GameDefinition d, MovePointer movePtr);

    public final Move getMove(Graph g, GameDefinition d) {
        ImmutableGraph ig = new ImmutableGraph(g);
        PlayerRunnable runnable = new PlayerRunnable(this, ig, d);

        Move m = null;

        try {
            Thread t = new Thread(runnable);
            t.start();
            t.join(d.getExecution());
            this.interrupt();
            m = runnable.getMovePointer().recall();
            int count = 0;
            while (t.isAlive()) {
                if (count > 0) {
                    log.warn(Finals.L_PLAYER_WAITING, count * d.getExecution() / 1000, this.getClass().getSimpleName());
                }
                t.join(d.getExecution());
                count++;
            }
            this.isInterrupted(); // This is called to clear the interrupt flag
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return m;
    }

    public Player putDefaultOptions() {
        return this;
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
}
