package gr.james.socialinfluence.game;

import gr.james.socialinfluence.algorithms.scoring.DeGrootOld;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.states.DoubleGraphState;

@Deprecated
public class GameOld {
    private Graph g;
    private Move playerAMove;
    private Move playerBMove;

    public GameOld(Graph g) {
        this.g = g;
        this.playerAMove = new Move();
        this.playerBMove = new Move();
    }

    public static GameResult runPlayers(Player a, Player b, Graph g, GameDefinition d, double deDrootEpsilon) {
        GameOld game = new GameOld(g);
        game.setPlayer(PlayerEnum.A, a.getMove(g, d));
        game.setPlayer(PlayerEnum.B, b.getMove(g, d));
        return game.runGame(d, deDrootEpsilon);
    }

    public static GameResult runPlayers(Player a, Player b, Graph g, GameDefinition d) {
        GameOld game = new GameOld(g);
        game.setPlayer(PlayerEnum.A, a.getMove(g, d));
        game.setPlayer(PlayerEnum.B, b.getMove(g, d));
        return game.runGame(d);
    }

    public Move getPlayerAMove() {
        return playerAMove;
    }

    public GameOld setPlayerAMove(Move move) {
        return setPlayer(PlayerEnum.A, move);
    }

    public Move getPlayerBMove() {
        return playerBMove;
    }

    public GameOld setPlayerBMove(Move move) {
        return setPlayer(PlayerEnum.B, move);
    }

    public GameOld setPlayer(PlayerEnum player, Move move) {
        if (player == PlayerEnum.A) {
            this.playerAMove = move.deepCopy();
        } else {
            this.playerBMove = move.deepCopy();
        }
        return this;
    }

    private GameOld swapPlayers() {
        Move tmp = this.playerAMove;
        this.playerAMove = this.playerBMove;
        this.playerBMove = tmp;
        return this;
    }

    private GraphState<Double> runPrimitiveGame(Double deGrootEpsilon) {
        Vertex playerA = this.g.addVertex();
        Vertex playerB = this.g.addVertex();

        this.g.addEdge(playerA, playerA);
        this.g.addEdge(playerB, playerB);

        for (Vertex v : this.playerAMove) {
            g.addEdge(v, playerA).setWeight(this.playerAMove.getWeight(v));
        }

        for (Vertex v : this.playerBMove) {
            g.addEdge(v, playerB).setWeight(this.playerBMove.getWeight(v));
        }

        GraphState<Double> initialOpinions = new DoubleGraphState(g, Finals.DEFAULT_GAME_OPINIONS);

        initialOpinions.put(playerA, 0.0);
        initialOpinions.put(playerB, 1.0);

        GraphState<Double> lastState = DeGrootOld.execute(g, initialOpinions, deGrootEpsilon, false);

        this.g.removeVertex(playerA);
        this.g.removeVertex(playerB);

        return lastState;
    }

    public GameResult runGame(GameDefinition d, double deGrootEpsilon) {
        if (d != null) {
            if (this.playerAMove.getVerticesCount() > d.getActions()) {
                String oldMove = this.playerAMove.toString();
                this.playerAMove.sliceMove(d.getActions());
                Finals.LOG.warn(Finals.L_GAME_MOVE_EXCEED, oldMove, d.getActions(), this.playerAMove.toString());
            }
            if (this.playerBMove.getVerticesCount() > d.getActions()) {
                String oldMove = this.playerBMove.toString();
                this.playerBMove.sliceMove(d.getActions());
                Finals.LOG.warn(Finals.L_GAME_MOVE_EXCEED, oldMove, d.getActions(), this.playerBMove.toString());
            }

            this.playerAMove.normalizeWeights(d.getBudget());
            this.playerBMove.normalizeWeights(d.getBudget());
        }

        /* If one of the players didn't submit a move, the other one is obviously the winner */
        if ((this.playerAMove.getVerticesCount() == 0) ^ (this.playerBMove.getVerticesCount() == 0)) {
            Finals.LOG.warn(Finals.L_GAME_EMPTY_MOVE);
            Vertex s1 = new Vertex();
            Vertex s2 = new Vertex();
            if (this.playerAMove.getVerticesCount() > 0) {
                GraphState<Double> gs = new DoubleGraphState(g, 0.0);
                gs.put(s1, 0.0);
                gs.put(s2, 1.0);
                return new GameResult(-1, gs, this.playerAMove, this.playerBMove);
            } else if (this.playerBMove.getVerticesCount() > 0) {
                GraphState<Double> gs = new DoubleGraphState(g, 1.0);
                gs.put(s1, 0.0);
                gs.put(s2, 1.0);
                return new GameResult(1, gs, this.playerAMove, this.playerBMove);
            }
        }

        /* If moves are both empty or equal, it's obviously a draw */
        if (this.playerAMove.equals(this.playerBMove)) {
            Finals.LOG.info(Finals.L_GAME_EMPTY_MOVES);
            Vertex s1 = new Vertex();
            Vertex s2 = new Vertex();
            GraphState<Double> gs = new DoubleGraphState(g, 0.5);
            gs.put(s1, 0.0);
            gs.put(s2, 1.0);
            return new GameResult(0, gs, this.playerAMove, this.playerBMove);
        }

        GraphState<Double> b = this.swapPlayers().runPrimitiveGame(deGrootEpsilon);
        GraphState<Double> a = this.swapPlayers().runPrimitiveGame(deGrootEpsilon);

        double am = a.getMean() - 0.5;
        double bm = b.getMean() - 0.5;

        int score;

        if (am * bm > 0) {
            Finals.LOG.warn("am * bm > 0");
            score = 0;
        } else if (am * bm == 0.0) {
            Finals.LOG.warn("am * bm == 0.0");
            score = 0;
        } else {
            score = Double.compare(am, 0);
        }

        return new GameResult(score, a, this.playerAMove, this.playerBMove);
    }

    public GameResult runGame(GameDefinition d) {
        return runGame(d, Finals.DEFAULT_GAME_PRECISION);
    }
}
