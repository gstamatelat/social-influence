package gr.james.influence.game;

import gr.james.influence.api.Graph;
import gr.james.influence.graph.ImmutableGraph;
import gr.james.influence.util.Finals;
import gr.james.influence.util.Helper;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected static final Logger log = Finals.LOG; // TODO: This should probably be private

    private final Object lock = new Object();
    private boolean interrupted = false;

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
