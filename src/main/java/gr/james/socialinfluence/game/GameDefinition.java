package gr.james.socialinfluence.game;

public class GameDefinition {
    private int numOfMoves;
    private double budget;
    private long execution;
    private boolean tournament;

    public GameDefinition(int numOfMoves, double budget, long execution, boolean tournament) {
        this.numOfMoves = numOfMoves;
        this.budget = budget;
        this.execution = execution;
        this.tournament = tournament;
    }

    public int getNumOfMoves() {
        return this.numOfMoves;
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
        return String.format("{numOfMoves=%d, budget=%f, execution=%d, tournament=%b}", numOfMoves, budget, execution, tournament);
    }
}