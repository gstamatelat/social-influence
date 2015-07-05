package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.Player;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.MovePointer;

public class ExceptionPlayer extends Player {
    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        throw new RuntimeException("Test exception from ExceptionPlayer");
    }
}
