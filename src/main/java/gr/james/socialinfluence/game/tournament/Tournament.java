package gr.james.socialinfluence.game.tournament;

import ch.qos.logback.classic.Level;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.api.GraphImporter;
import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.util.Conditions;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.exceptions.GraphException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tournament {
    public static final int WIN = 4;
    public static final int LOSE = 1;
    public static final int DRAW = 2;

    private TournamentHandler handler;
    private Map<Player, Integer> players = new HashMap<>();

    public Tournament(TournamentHandler handler) {
        this.handler = handler;
    }

    public Tournament() {
        this.handler = null;
    }

    public void addPlayer(Player p) {
        players.put(Conditions.requireNonNull(p), 0);
    }

    public void addPlayers(Player... players) {
        for (Player p : players) {
            addPlayer(p);
        }
    }

    public void run(GraphImporter i, InputStream s, GameDefinition d) {
        run(() -> {
            try {
                return i.from(s);
            } catch (IOException e) {
                throw new GraphException(e.getMessage());
            }
        }, d);
    }

    public Map<Player, Integer> getScores() {
        return Collections.unmodifiableMap(players);
    }

    private void resetScores() {
        players.replaceAll((player, integer) -> 0);
    }

    public void run(GraphGenerator generator, GameDefinition d) {
        resetScores();

        ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) Finals.LOG;
        ch.qos.logback.classic.Level previousLevel = logbackLogger.getLevel();
        logbackLogger.setLevel(Level.ERROR);

        int done = 0;
        for (Player a : players.keySet()) {
            for (Player b : players.keySet()) {
                if (a != b) {
                    GameResult r = Game.runPlayers(a, b, generator.create(), d);
                    if (r.score < 0) {
                        players.put(a, players.get(a) + WIN);
                        players.put(b, players.get(b) + LOSE);
                    } else if (r.score > 0) {
                        players.put(a, players.get(a) + LOSE);
                        players.put(b, players.get(b) + WIN);
                    } else {
                        players.put(a, players.get(a) + DRAW);
                        players.put(b, players.get(b) + DRAW);
                    }
                    if (handler != null) {
                        handler.progressChanged(++done, players.size() * (players.size() - 1));
                    }
                }
            }
        }

        logbackLogger.setLevel(previousLevel);
    }
}
