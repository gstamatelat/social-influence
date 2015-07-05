package gr.james.socialinfluence.game;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.helper.Finals;

public class PlayerRunnable implements Runnable {
    private Player p;
    private Graph g;
    private GameDefinition d;
    private MovePointer movePtr;

    public PlayerRunnable(Player p, Graph g, GameDefinition d) {
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
            Finals.LOG.warn(Finals.L_PLAYER_EXCEPTION, p.getClass().getSimpleName(), e, g, d);
        }
    }
}
