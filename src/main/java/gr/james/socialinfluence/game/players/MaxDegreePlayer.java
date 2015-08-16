package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.algorithms.iterators.GraphStateIterator;
import gr.james.socialinfluence.algorithms.scoring.Degree;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.game.Player;

public class MaxDegreePlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        Move m = new Move();
        GraphStateIterator<Integer> it = new GraphStateIterator<>(Degree.execute(g, true));
        while (m.getVerticesCount() < d.getActions()) {
            m.putVertex(it.next().getObject(), 1.0);
        }
        movePtr.submit(m);
        log.debug("{}", m);
    }
}
