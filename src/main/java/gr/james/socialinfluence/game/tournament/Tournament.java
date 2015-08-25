package gr.james.socialinfluence.game.tournament;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.GameResult;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.util.Conditions;

import java.util.*;
import java.util.stream.Collectors;

public class Tournament {
    public static final int WIN = 2;
    public static final int LOSE = 0;
    public static final int DRAW = 1;

    public static final int DEFAULT_ROUNDS = 1;
    public static final boolean DEFAULT_ONE_GRAPH_PER_ROUND = false;

    private TournamentHandler handler;
    private Set<Player> players = new HashSet<>();
    private Map<TournamentDefinition, Map<Player, Integer>> scores = new LinkedHashMap<>();

    public Tournament(TournamentHandler handler, Player... players) {
        this.handler = handler;
        for (Player p : players) {
            this.players.add(Conditions.requireNonNull(p));
        }
    }

    public Tournament(Player... players) {
        this(null, players);
    }

    public String getAllScoresInDsv(String delimiter) {
        Comparator<Player> pComparator = (o1, o2) -> o1.toString().compareTo(o2.toString());
        String csv = String.format("%s%s%s%s%s%s%s%s%s%n", "GRAPH", delimiter, "DEFINITION", delimiter, "ROUNDS",
                delimiter, "ONE_GRAPH_PER_ROUND", delimiter,
                players.stream().sorted(pComparator).map(Player::toString).collect(Collectors.joining(delimiter)));
        for (TournamentDefinition g : scores.keySet()) {
            String hilariousStreamExpression = scores.get(g).keySet().stream().sorted(pComparator)
                    .map(item -> scores.get(g).get(item).toString()).collect(Collectors.joining(delimiter));
            csv += String.format("\"%s\"%s\"%s\"%s%d%s%b%s%s%n", g.getGenerator().create(), delimiter,
                    g.getDefinition(), delimiter, g.getRounds(), delimiter, g.getOneGraphPerRound(), delimiter,
                    hilariousStreamExpression);
        }
        return csv;
    }

    public String getAllScoresInCsv() {
        return getAllScoresInDsv(",");
    }

    public Map<Player, Integer> run(GraphGenerator generator, GameDefinition d, int rounds, boolean oneGraphPerRound) {
        Map<Player, Integer> score = new HashMap<>();
        for (Player p : players) {
            score.put(p, 0);
        }

        int done = 0;
        List playersList = new ArrayList<>(players);
        for (int i = 0; i < rounds; i++) {
            Graph g = oneGraphPerRound ? generator.create() : null;
            for (Player a : players) {
                for (Player b : players) {
                    if (playersList.indexOf(a) > playersList.indexOf(b)) {
                        g = oneGraphPerRound ? g : generator.create();
                        GameResult r = Game.runPlayers(a, b, g, d);
                        if (r.score < 0) {
                            score.put(a, score.get(a) + WIN);
                            score.put(b, score.get(b) + LOSE);
                        } else if (r.score > 0) {
                            score.put(a, score.get(a) + LOSE);
                            score.put(b, score.get(b) + WIN);
                        } else {
                            score.put(a, score.get(a) + DRAW);
                            score.put(b, score.get(b) + DRAW);
                        }
                        if (handler != null) {
                            handler.progressChanged(++done, rounds * players.size() * (players.size() - 1) / 2);
                        }
                    }
                }
            }
        }

        scores.put(new TournamentDefinition(generator, d, rounds, oneGraphPerRound), score);

        return Collections.unmodifiableMap(score);
    }

    public Map<Player, Integer> run(GraphGenerator generator, GameDefinition d, int rounds) {
        return run(generator, d, rounds, DEFAULT_ONE_GRAPH_PER_ROUND);
    }

    public Map<Player, Integer> run(GraphGenerator generator, GameDefinition d) {
        return run(generator, d, DEFAULT_ROUNDS);
    }
}
