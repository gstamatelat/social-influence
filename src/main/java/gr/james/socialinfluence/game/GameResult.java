package gr.james.socialinfluence.game;

import gr.james.socialinfluence.api.GraphState;

public class GameResult {
    public int score = 0;
    public GraphState<Double> fullState = null;
    public Move m1;
    public Move m2;

    public GameResult(int score, GraphState<Double> fullState, Move m1, Move m2) {
        this.score = score;
        this.fullState = fullState;
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override
    public String toString() {
        return String.format("{score=%d,state=%.2f}", score, fullState.getMean());
    }
}
