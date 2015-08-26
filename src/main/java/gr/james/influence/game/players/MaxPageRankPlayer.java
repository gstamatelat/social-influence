package gr.james.influence.game.players;

import gr.james.influence.algorithms.iterators.GraphStateIterator;
import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.api.Graph;
import gr.james.influence.game.GameDefinition;
import gr.james.influence.game.Move;
import gr.james.influence.game.MovePointer;
import gr.james.influence.game.Player;

public class MaxPageRankPlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        Move m = new Move();
        GraphStateIterator<Double> it = new GraphStateIterator<>(PageRank.execute(g, 0.15));
        while (m.getVerticesCount() < d.getActions()) {
            m.putVertex(it.next().getObject(), 1.0);
        }
        movePtr.submit(m);
        log.info("{}", m);
    }
}
