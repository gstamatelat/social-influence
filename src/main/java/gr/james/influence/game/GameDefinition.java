package gr.james.influence.game;

public class GameDefinition {
    private int actions;
    private double budget;
    private long execution;
    private double precision;

    public GameDefinition(int actions, double budget, long execution, double precision) {
        this.actions = actions;
        this.budget = budget;
        this.execution = execution;
        this.precision = precision;
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

    public double getPrecision() {
        return this.precision;
    }

    @Override
    public String toString() {
        return String.format("{actions=%d, budget=%.2f, execution=%d, precision=%e}",
                actions,
                budget,
                execution,
                precision
        );
    }
}
