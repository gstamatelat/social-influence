package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;

public class SlowPlayer extends AbstractPlayer {
    @Override
    public void getMove() {
        long now = System.currentTimeMillis();

        while (System.currentTimeMillis() - now < 5 * this.d.getExecution()) {
            Move m = new Move();
            RandomVertexIterator rvi = new RandomVertexIterator(this.g);
            while (m.getVerticesCount() < this.d.getActions()) {
                m.putVertex(rvi.next(), 1.0);
            }
            this.movePtr.submit(m);
            log.info("Slow player: {}", m);
        }
    }
}