package gr.james.influence.game.tournament;

import gr.james.influence.api.GraphGenerator;
import gr.james.influence.game.GameDefinition;

public class TournamentDefinition {
    private GraphGenerator generator;
    private GameDefinition definition;
    private int rounds;
    private boolean oneGraphPerRound;

    public TournamentDefinition(GraphGenerator generator, GameDefinition definition, int rounds, boolean oneGraphPerRound) {
        this.generator = generator;
        this.definition = definition;
        this.rounds = rounds;
        this.oneGraphPerRound = oneGraphPerRound;
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

    public boolean getOneGraphPerRound() {
        return oneGraphPerRound;
    }
}
