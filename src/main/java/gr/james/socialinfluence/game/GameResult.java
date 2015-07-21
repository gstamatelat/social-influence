package gr.james.socialinfluence.game;

import gr.james.socialinfluence.collections.states.DoubleGraphState;

public class GameResult {
    public int score = 0;
    public DoubleGraphState fullState = null;

    public GameResult(int score, DoubleGraphState fullState) {
        this.score = score;
        this.fullState = fullState;
    }

    @Override
    public String toString() {
        return String.format("{score=%d,state=%.2f}", score, fullState.getMean());
    }
}
