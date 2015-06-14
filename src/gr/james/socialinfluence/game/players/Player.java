package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.Helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Player implements Runnable {
    protected Map<String, String> options = new HashMap<String, String>();

    protected Graph g;
    protected GameDefinition d;
    protected MovePointer movePtr = new MovePointer();

    private boolean interrupted = false;

    public Player() {
        putDefaultOptions();
    }

    /**
     * <p>Tests whether the player has been interrupted. The game mechanism requests player interruption when the
     * player has exhausted the available time for execution. When this flag is set to {@code true}, further moves
     * submitted by the player will be ignored and this is an indication that the player must terminate gracefully.
     * This flag is an alternative way of time tracking besides {@link GameDefinition#getExecution() getExecution()}
     * method of field {@link #d}.</p>
     *
     * @return {@code true} if the player was interrupted and must terminate, otherwise {@code false}
     */
    public boolean isInterrupted() {
        return this.interrupted;
    }

    public abstract void getMove();

    public void run() {
        try {
            getMove();
        } catch (Exception e) {
            Helper.logError(Finals.E_PLAYER_EXCEPTION, this.getClass().getSimpleName(), e, g, d);
        }
    }

    public final Move findMove(Graph g, GameDefinition d) {
        this.g = g;
        this.d = d;

        Move m = null;

        try {
            Thread t = new Thread(this);
            this.interrupted = false;
            t.start();
            t.join(this.d.getExecution());
            this.interrupted = true;
            m = this.movePtr.get();
            int count = 0;
            while (t.isAlive()) {
                if (count > 0) {
                    Helper.logError(Finals.W_PLAYER_WAITING, count * d.getExecution() / 1000, this.getClass().getSimpleName());
                }
                t.join(d.getExecution());
                count++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return m;
    }

    protected Player putDefaultOptions() {
        return this;
    }

    public final Player setOption(String name, String value) {
        if (!this.options.containsKey(name)) {
            throw new GraphException(Finals.E_PLAYER_NO_PARAMETER, this.getClass().getSimpleName(), name);
        } else if (value == null) {
            throw new GraphException(Finals.E_PLAYER_OPTION_NULL);
        } else {
            this.options.put(name, value);
        }
        return this;
    }

    public final Map<String, String> getOptions() {
        return Collections.unmodifiableMap(this.options);
    }
}