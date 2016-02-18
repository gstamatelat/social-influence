package gr.james.influence.game;

import gr.james.influence.algorithms.scoring.DeGroot;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;

public final class Game {
    private Game() {
    }

    private static GraphState<Double> runPrimitiveGame(Graph g, Move playerAMove, Move playerBMove, double deGrootEpsilon) {
        Vertex playerA = g.addVertex();
        Vertex playerB = g.addVertex();

        g.addEdge(playerA, playerA);
        g.addEdge(playerB, playerB);

        for (Vertex v : playerAMove) {
            g.addEdge(v, playerA, playerAMove.getWeight(v));
        }

        for (Vertex v : playerBMove) {
            g.addEdge(v, playerB, playerBMove.getWeight(v));
        }

        GraphState<Double> initialOpinions = new GraphState<>(g, Finals.DEFAULT_GAME_OPINIONS);

        initialOpinions.put(playerA, 0.0);
        initialOpinions.put(playerB, 1.0);

        GraphState<Double> lastState = DeGroot.execute(g, initialOpinions, deGrootEpsilon);

        g.removeVertex(playerA);
        g.removeVertex(playerB);

        lastState.remove(playerA);
        lastState.remove(playerB);

        return lastState;
    }

    public static GameResult runPlayers(Player a, Player b, Graph g, GameDefinition d) {
        return runMoves(g, d, a.getMove(g, d), b.getMove(g, d));
    }

    public static GameResult runMoves(Graph g, GameDefinition d, Move playerAMove, Move playerBMove) {
        if (d != null) {
            playerAMove = playerAMove.deepCopy();
            playerBMove = playerBMove.deepCopy();

            if (playerAMove.getVerticesCount() > d.getActions()) {
                String oldMove = playerAMove.toString();
                playerAMove.sliceMove(d.getActions());
                Finals.LOG.warn(Finals.L_GAME_MOVE_EXCEED, oldMove, d.getActions(), playerAMove.toString());
            }
            if (playerBMove.getVerticesCount() > d.getActions()) {
                String oldMove = playerBMove.toString();
                playerBMove.sliceMove(d.getActions());
                Finals.LOG.warn(Finals.L_GAME_MOVE_EXCEED, oldMove, d.getActions(), playerBMove.toString());
            }

            playerAMove.normalizeWeights(d.getBudget());
            playerBMove.normalizeWeights(d.getBudget());
        }

        /* If one of the players didn't submit a move, the other one is obviously the winner */
        /*if ((this.playerAMove.getVerticesCount() == 0) ^ (this.playerBMove.getVerticesCount() == 0)) {
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
        }*/

        /* If moves are both empty or equal, it's obviously a draw */
        /*if (this.playerAMove.equals(this.playerBMove)) {
            Finals.LOG.info(Finals.L_GAME_EMPTY_MOVES);
            Vertex s1 = new Vertex();
            Vertex s2 = new Vertex();
            GraphState<Double> gs = new DoubleGraphState(g, 0.5);
            gs.put(s1, 0.0);
            gs.put(s2, 1.0);
            return new GameResult(0, gs, this.playerAMove, this.playerBMove);
        }*/

        double precision = (d == null) ? Finals.DEFAULT_DEGROOT_PRECISION : d.getPrecision();

        GraphState<Double> a = runPrimitiveGame(g, playerAMove, playerBMove, precision);
        GraphState<Double> b = runPrimitiveGame(g, playerBMove, playerAMove, precision);

        double am = a.getAverage() - 0.5;
        double bm = b.getAverage() - 0.5;

        /*if (am + bm != 0.0) {
            Finals.LOG.debug("am + bm != 0");
        }*/

        double cm = (am - bm) / 2;

        int score;

        if (Math.abs(cm) <= 2 * precision) {
            score = 0;
        } else if (cm > 0) {
            score = 1;
        } else {
            score = -1;
        }

        /*if (am * bm > 0) {
            Finals.LOG.warn("am * bm > 0");
            score = 0;
        } else if (am * bm == 0.0) {
            if (am + bm != 0.0) {
                Finals.LOG.warn("am * bm == 0.0");
            }
            score = 0;
        } else {
            score = Double.compare(am, 0);
        }*/

        return new GameResult(score, a, playerAMove, playerBMove);
    }

    /*public static GameResult runMoves(Graph g, GameDefinition d, Move playerAMove, Move playerBMove) {
        return runMoves(g, d, playerAMove, playerBMove, Finals.DEFAULT_GAME_PRECISION);
    }*/
}
