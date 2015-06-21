package gr.james.socialinfluence.game;

import gr.james.socialinfluence.collections.GraphState;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.DeGroot;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.GraphException;
import gr.james.socialinfluence.helper.Helper;

public class Game {
    private Graph g;
    private Move playerAMove;
    private Move playerBMove;

    public Game(Graph g) {
        this.g = g;
        this.playerAMove = new Move();
        this.playerBMove = new Move();
    }

    public Move getPlayerAMove() {
        return playerAMove;
    }

    public Move getPlayerBMove() {
        return playerBMove;
    }

    public Game setPlayer(PlayerEnum player, Move move) {
        if (player == PlayerEnum.A) {
            this.playerAMove = move.deepCopy();
        } else {
            this.playerBMove = move.deepCopy();
        }
        return this;
    }

    public Game swapPlayers() {
        Move tmp = this.playerAMove;
        this.playerAMove = this.playerBMove;
        this.playerBMove = tmp;
        return this;
    }

    private GraphState runPrimitiveGame(Double deGrootEpsilon) {
        Vertex playerA = this.g.addVertex();
        Vertex playerB = this.g.addVertex();

        this.g.addEdge(playerA, playerA);
        this.g.addEdge(playerB, playerB);

        for (MovePoint e : this.playerAMove) {
            try {
                g.addEdge(e.vertex, playerA).setWeight(e.weight);
            } catch (GraphException x) {
                Helper.logError(Finals.W_GAME_INVALID_VERTEX);
            }
        }

        for (MovePoint e : this.playerBMove) {
            try {
                g.addEdge(e.vertex, playerB).setWeight(e.weight);
            } catch (GraphException x) {
                Helper.logError(Finals.W_GAME_INVALID_VERTEX);
            }
        }

        GraphState initialOpinions = new GraphState(g, 0.5);

        initialOpinions.put(playerA, 0.0);
        initialOpinions.put(playerB, 1.0);

        GraphState lastState = DeGroot.execute(g, initialOpinions, deGrootEpsilon, false);

        this.g.removeVertex(playerA).removeVertex(playerB);

        return lastState;
    }

    public GameResult runGame(GameDefinition d, double deGrootEpsilon) {
        if (d != null) {
            if (this.playerAMove.getVerticesCount() > d.getNumOfMoves()) {
                String oldMove = this.playerAMove.toString();
                this.playerAMove.sliceMove(d.getNumOfMoves());
                Helper.logError(Finals.W_GAME_MOVE_EXCEED, oldMove, d.getNumOfMoves(), this.playerAMove.toString());
            }
            if (this.playerBMove.getVerticesCount() > d.getNumOfMoves()) {
                String oldMove = this.playerBMove.toString();
                this.playerBMove.sliceMove(d.getNumOfMoves());
                Helper.logError(Finals.W_GAME_MOVE_EXCEED, oldMove, d.getNumOfMoves(), this.playerBMove.toString());
            }

            this.playerAMove.normalizeWeights(d.getBudget());
            this.playerBMove.normalizeWeights(d.getBudget());
        }

        /* If one of the players didn't submit a move, the other one is obviously the winner */
        if ((this.playerAMove.getVerticesCount() == 0) ^ (this.playerBMove.getVerticesCount() == 0)) {
            Helper.logError(Finals.W_GAME_EMPTY_MOVE);
            Vertex s1 = g.addVertex();
            Vertex s2 = g.addVertex();
            g.removeVertex(s1).removeVertex(s2);
            if (this.playerAMove.getVerticesCount() > 0) {
                GraphState gs = new GraphState(g, 0.0);
                gs.put(s1, 0.0);
                gs.put(s2, 1.0);
                return new GameResult(-1, gs);
            } else if (this.playerBMove.getVerticesCount() > 0) {
                GraphState gs = new GraphState(g, 1.0);
                gs.put(s1, 0.0);
                gs.put(s2, 1.0);
                return new GameResult(1, gs);
            }
        }

        /* If moves are both empty or equal, it's obviously a draw */
        if (this.playerAMove.equals(this.playerBMove)) {
            Vertex s1 = g.addVertex();
            Vertex s2 = g.addVertex();
            g.removeVertex(s1).removeVertex(s2);
            GraphState gs = new GraphState(g, 0.5);
            gs.put(s1, 0.0);
            gs.put(s2, 1.0);
            return new GameResult(0, gs);
        }

        GraphState b = this.swapPlayers().runPrimitiveGame(deGrootEpsilon);
        GraphState a = this.swapPlayers().runPrimitiveGame(deGrootEpsilon);

        double am = a.getMean() - 0.5;
        double bm = b.getMean() - 0.5;

        int score;

        if (am * bm > 0) {
            score = 0;
        } else if (am * bm == 0.0) {
            score = 0;
        } else {
            score = Double.compare(am, 0);
        }

        return new GameResult(score, a);
    }

    public GameResult runGame(GameDefinition d) {
        return runGame(d, Finals.DEFAULT_EPSILON);
    }
}