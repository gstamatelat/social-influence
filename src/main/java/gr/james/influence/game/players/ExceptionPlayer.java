package gr.james.influence.game.players;

import gr.james.influence.api.Graph;
import gr.james.influence.game.GameDefinition;
import gr.james.influence.game.MovePointer;
import gr.james.influence.game.Player;

public class ExceptionPlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        g.addVertex();
    }
}
