package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;

public class RandomPlayer extends Player {
    @Override
    public void getMove() {
        Move m = new Move();
        RandomVertexIterator rvi = new RandomVertexIterator(this.g);
        while (m.getVerticesCount() < this.d.getActions()) {
            m.putVertex(rvi.next(), 1.0);
        }
        this.movePtr.submit(m);
        if (!this.d.getTournament()) {
            log.info("RandomG player: {}", m);
        }
    }
}