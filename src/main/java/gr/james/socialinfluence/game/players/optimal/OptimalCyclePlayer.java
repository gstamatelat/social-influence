package gr.james.socialinfluence.game.players.optimal;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.graph.Vertex;

public class OptimalCyclePlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        /* This player only works in the cycle graph */
        if (!g.getGraphType().equals("Cycle")) {
            log.warn("Graph {} is not a cycle. OptimalCyclePlayer only works for cycles. Aborting now.", g);
            return;
        }

        /* Optimal spreading distance */
        final double period = (double) g.getVerticesCount() / d.getActions();

        /* Log some graph info */
        log.info("Working on a {}-vertex cycle graph for {} actions. Optimal splitting is {} vertices.",
                g.getVerticesCount(), d.getActions(), period);

        /* Initialize a new move without any vertices */
        Move m = new Move();

        /* Start with the first vertex */
        double c = 0;

        /* Repeat until m is full */
        while (m.getVerticesCount() < d.getActions()) {
            /* Select the vertex that corresponds to round(c) */
            Vertex n = g.getVertexFromIndex((int) (c + 0.5));

            /* Add the vertex to m */
            m.putVertex(n, 1.0);

            /* Advance by period vertices */
            c += period;
        }

        /* Log move for demonstration */
        log.info("Final move: {}", m);

        /* Submit the move */
        movePtr.submit(m);
    }
}
