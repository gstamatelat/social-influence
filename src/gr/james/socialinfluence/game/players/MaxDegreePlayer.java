package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.PageRankIterator;
import gr.james.socialinfluence.helper.Helper;

public class MaxDegreePlayer extends Player {
    @Override
    public void getMove() {
        // TODO: This isn't degree player
        Move m = new Move();
        PageRankIterator pri = new PageRankIterator(g, 0.15);
        while (m.getVerticesCount() < d.getNumOfMoves()) {
            m.putVertex(pri.next(), 1.0);
        }
        if (!this.d.getTournament()) {
            Helper.log("Max PageRank player: " + m);
        }
        movePtr.set(m);
    }
}