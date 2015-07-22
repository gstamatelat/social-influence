package gr.james.socialinfluence.game;

import gr.james.socialinfluence.api.GraphState;

public class GameResult {
    public int score = 0;
    public GraphState<Double> fullState = null;

    public GameResult(int score, GraphState<Double> fullState) {
        this.score = score;
        this.fullState = fullState;
    }

    @Override
    public String toString() {
        return String.format("{score=%d,state=%.2f}", score, fullState.getMean());
    }
}
