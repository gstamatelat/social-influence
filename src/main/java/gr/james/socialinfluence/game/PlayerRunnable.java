package gr.james.socialinfluence.game;

import gr.james.socialinfluence.graph.ImmutableGraph;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.Helper;

public class PlayerRunnable implements Runnable {
    private Player p;
    private ImmutableGraph g;
    private GameDefinition d;
    private MovePointer movePtr;

    public PlayerRunnable(Player p, ImmutableGraph g, GameDefinition d) {
        this.p = p;
        this.g = g;
        this.d = d;
        this.movePtr = new MovePointer();
    }

    public MovePointer getMovePointer() {
        return this.movePtr;
    }

    @Override
    public void run() {
        try {
            p.suggestMove(g, d, movePtr);
        } catch (Exception e) {
            Finals.LOG.error(Finals.L_PLAYER_EXCEPTION, p.getClass().getSimpleName(), g, d,
                    Helper.getExceptionString(e));
        }
    }
}
