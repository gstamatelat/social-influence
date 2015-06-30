package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.algorithms.iterators.PageRankIterator;

public class MaxDegreePlayer extends AbstractPlayer {
    @Override
    public void getMove() {
        // TODO: This isn't degree player
        Move m = new Move();
        PageRankIterator pri = new PageRankIterator(this.g, 0.15);
        while (m.getVerticesCount() < d.getActions()) {
            m.putVertex(pri.next(), 1.0);
        }
        this.movePtr.submit(m);
        if (!this.d.getTournament()) {
            log.info("Max PageRank player: {}", m);
        }
    }
}