package gr.james.socialinfluence.game;

public class GameDefinition {
    private int actions;
    private double budget;
    private long execution;

    public GameDefinition(int actions, double budget, long execution) {
        this.actions = actions;
        this.budget = budget;
        this.execution = execution;
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

    @Override
    public String toString() {
        return String.format("{actions=%d, budget=%.2f, execution=%d}", actions, budget, execution);
    }
}
