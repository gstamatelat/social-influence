package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.algorithms.iterators.GraphStateIterator;
import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.graph.ImmutableGraph;

public class MaxPageRankPlayer extends Player {
    @Override
    public void suggestMove(ImmutableGraph g, GameDefinition d, MovePointer movePtr) {
        Move m = new Move();
        GraphStateIterator<Double> it = new GraphStateIterator<>(PageRank.execute(g, 0.15));
        while (m.getVerticesCount() < d.getActions()) {
            m.putVertex(it.next().getObject(), 1.0);
        }
        movePtr.submit(m);
        log.info("{}", m);
    }
}
