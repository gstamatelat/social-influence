package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.PageRankIterator;
import gr.james.socialinfluence.helper.Helper;

public class MaxPageRankPlayer extends Player {
    @Override
    public void getMove() {
        Move m = new Move();
        PageRankIterator pri = new PageRankIterator(g, 0.15);
        while (m.getVerticesCount() < d.getNumOfMoves()) {
            m.putVertex(pri.next(), 1.0);
        }
        m.normalizeWeights(d.getBudget());
        if (!this.d.getTournament()) {
            Helper.log("Max PageRank player: " + m);
        }
        movePtr.set(m);
    }
}