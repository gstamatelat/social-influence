package gr.james.socialinfluence.game;

import gr.james.socialinfluence.collections.GraphState;

public class GameResult {
    public int score = 0;
    public GraphState fullState = null;

    public GameResult(int score, GraphState fullState) {
        this.score = score;
        this.fullState = fullState;
    }
}