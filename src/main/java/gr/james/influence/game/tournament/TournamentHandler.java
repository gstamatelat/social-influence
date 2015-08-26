package gr.james.influence.game.tournament;

@FunctionalInterface
public interface TournamentHandler {
    void progressChanged(int done, int total);
}
