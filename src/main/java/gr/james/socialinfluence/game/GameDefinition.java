package gr.james.socialinfluence.game;

public class GameDefinition {
    private int actions;
    private double budget;
    private long execution;
    private boolean tournament;

    public GameDefinition(int actions, double budget, long execution, boolean tournament) {
        this.actions = actions;
        this.budget = budget;
        this.execution = execution;
        this.tournament = tournament;
    }

    public int getActions() {
        return this.actions;
    }

    public double getBudget() {
        return this.budget;
    }

    public long getExecution() {
        return this.execution;
    }

    public boolean getTournament() {
        return this.tournament;
    }

    @Override
    public String toString() {
        return String.format("{actions=%d, budget=%f, execution=%d, tournament=%b}", actions, budget, execution, tournament);
    }
}