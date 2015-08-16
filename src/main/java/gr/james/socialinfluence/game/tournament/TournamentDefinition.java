package gr.james.socialinfluence.game.tournament;

import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.game.GameDefinition;

public class TournamentDefinition {
    private GraphGenerator generator;
    private GameDefinition definition;
    private int rounds;

    public TournamentDefinition(GraphGenerator generator, GameDefinition definition, int rounds) {
        this.generator = generator;
        this.definition = definition;
        this.rounds = rounds;
    }

    public GraphGenerator getGenerator() {
        return generator;
    }

    public GameDefinition getDefinition() {
        return definition;
    }

    public int getRounds() {
        return rounds;
    }
}
