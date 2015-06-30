package gr.james.socialinfluence.api;

import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.graph.Graph;

import java.util.Map;

public interface Player {
    Player setOption(String name, String value);

    Map<String, String> getOptions();

    Player putDefaultOptions();

    Move findMove(Graph g, GameDefinition d);

    void getMove();

    boolean isInterrupted();
}