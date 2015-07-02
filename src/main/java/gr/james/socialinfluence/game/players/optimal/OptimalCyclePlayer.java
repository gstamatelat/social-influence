package gr.james.socialinfluence.game.players.optimal;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.players.AbstractPlayer;
import gr.james.socialinfluence.graph.Vertex;

public class OptimalCyclePlayer extends AbstractPlayer {
    @Override
    public void getMove() {
        /* This player only works in the cycle graph */
        if (!this.g.getName().equals("cycle")) {
            log.warn("Graph {} is not a cycle. OptimalCyclePlayer only works for cycles. Aborting now.", g);
            return;
        }

        /* Optimal spreading distance */
        final double period = (double) this.g.getVerticesCount() / this.d.getActions();

        /* Log some graph info */
        log.info("Working on a {}-vertex cycle graph for {} actions. Optimal splitting is {} vertices.",
                g.getVerticesCount(), this.d.getActions(), period);

        /* Initialize a new move without any vertices */
        Move m = new Move();

        /* Start with the first vertex */
        double c = 0;

        /* Repeat until m is full */
        while (m.getVerticesCount() < this.d.getActions()) {
            /* Select the vertex that corresponds to round(c) */
            Vertex n = this.g.getVertexFromIndex((int) (c + 0.5));

            /* Add the vertex to m */
            m.putVertex(n, 1.0);

            /* Advance by period vertices */
            c += period;
        }

        /* We can print stuff if we aren't competing in tournament */
        log.info("Final move: {}", m);

        /* Submit the move */
        this.movePtr.submit(m);
    }
}