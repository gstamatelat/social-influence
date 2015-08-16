package gr.james.socialinfluence.game.tournament;

@FunctionalInterface
public interface TournamentHandler {
    void progressChanged(int done, int total);
}
