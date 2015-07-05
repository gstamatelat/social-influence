package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.graph.algorithms.iterators.PageRankIterator;

public class MaxPageRankPlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        Move m = new Move();
        PageRankIterator pri = new PageRankIterator(g, 0.15);
        while (m.getVerticesCount() < d.getActions()) {
            m.putVertex(pri.next(), 1.0);
        }
        movePtr.submit(m);
        log.info("{}", m);
    }
}
