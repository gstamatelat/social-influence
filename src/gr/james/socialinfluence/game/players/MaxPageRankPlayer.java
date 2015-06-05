package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.PageRankIterator;
import gr.james.socialinfluence.helper.Helper;

public class MaxPageRankPlayer extends Player {
    @Override
    public void getMove() {
        Move m = new Move();
        PageRankIterator pri = new PageRankIterator(this.g, 0.15);
        while (m.getVerticesCount() < this.d.getNumOfMoves()) {
            m.putVertex(pri.next(), 1.0);
        }
        this.movePtr.set(m);
        if (!this.d.getTournament()) {
            Helper.log("Max PageRank player: " + m);
        }
    }
}