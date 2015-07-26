package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.graph.ImmutableGraph;

public class ExceptionPlayer extends Player {
    @Override
    public void suggestMove(ImmutableGraph g, GameDefinition d, MovePointer movePtr) {
        throw new RuntimeException("Test exception from ExceptionPlayer");
    }
}
