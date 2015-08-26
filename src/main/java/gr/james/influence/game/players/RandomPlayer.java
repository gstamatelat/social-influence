package gr.james.influence.game.players;

import gr.james.influence.algorithms.iterators.RandomVertexIterator;
import gr.james.influence.api.Graph;
import gr.james.influence.game.GameDefinition;
import gr.james.influence.game.Move;
import gr.james.influence.game.MovePointer;
import gr.james.influence.game.Player;

public class RandomPlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        Move m = new Move();
        RandomVertexIterator rvi = new RandomVertexIterator(g);
        while (m.getVerticesCount() < d.getActions()) {
            m.putVertex(rvi.next(), 1.0);
        }
        movePtr.submit(m);
        log.info("{}", m);
    }
}
