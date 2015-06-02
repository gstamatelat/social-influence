package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.helper.Helper;

public class RandomPlayer extends Player {
    @Override
    public void getMove() {
        Move m = new Move();
        RandomVertexIterator rvi = new RandomVertexIterator(g);
        while (m.getVerticesCount() < d.getNumOfMoves()) {
            m.putVertex(rvi.next(), 1.0);
        }
        m.normalizeWeights(d.getBudget());
        if (!this.d.getTournament()) {
            Helper.log("RandomG player: " + m);
        }
        movePtr.set(m);
    }
}