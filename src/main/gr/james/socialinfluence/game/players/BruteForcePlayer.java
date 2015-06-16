package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.game.Game;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePoint;
import gr.james.socialinfluence.game.PlayerEnum;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.graph.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

import java.util.HashSet;

/**
 * <p>Implements a simple brute-force player.</p>
 * <p>Options:</p>
 * <ul>
 * <li>weight_levels: Default value is 100. [ADD STUFF]</li>
 * <li>epsilon: Default value is Finals.DEFAULT_EPSILON. [ADD STUFF]</li>
 * <li>clever: Instead of picking new moves completely at random, use a mutation, a move that is relatively close to
 * the previous move. Enabling this option should theoretically result in better results most of the time. This is
 * enabled by default.</li>
 * </ul>
 */
public class BruteForcePlayer extends Player {
    public static Move getRandomMove(Graph g, int numOfMoves, int weightLevels, Move lastMove, boolean clever) {
        if (!clever) {
            return getRandomMoveWithoutMutation(g, numOfMoves, weightLevels);
        } else {
            return getRandomMoveWithMutation(g, numOfMoves, weightLevels, lastMove);
        }
    }

    public static Move getRandomMoveWithoutMutation(Graph g, int numOfMoves, int weightLevels) {
        Move moves = new Move();
        RandomVertexIterator it = new RandomVertexIterator(g);
        while (moves.getVerticesCount() < numOfMoves) {
            // TODO: Something must be done to allow selecting less than numOfMoves vertices.
            moves.putVertex(it.next(), Finals.RANDOM.nextInt(weightLevels) + 1);
        }
        return moves;
    }

    public static Move getRandomMoveWithMutation(Graph g, int numOfMoves, int weightLevels, Move lastMove) {
        double jump_probability = 0.2; // TODO: Make it an option, or even better adaptive since it should depend on the diameter of the graph

        Move moves = new Move();
        // TODO: This loop is problematic. There is a chance that 2 nodes surf to the same node and thus create a move with less than numOfMoves nodes.
        // TODO: If this happens, it will never increase the nodes again
        for (MovePoint mp : lastMove) {
            Vertex v = mp.vertex;
            RandomSurferIterator randomSurfer = new RandomSurferIterator(g, 0.0, v);
            while (Finals.RANDOM.nextDouble() < jump_probability) {
                v = randomSurfer.next();
            }

            moves.putVertex(v, Finals.RANDOM.nextInt(weightLevels) + 1);
        }
        return moves;
    }

    @Override
    public void getMove() {
        Game game = new Game(this.g);

        HashSet<Move> movesHistory = new HashSet<Move>();
        HashSet<Move> moveDraws = new HashSet<Move>();

        Move bestMove = getRandomMove(this.g, this.d.getNumOfMoves(), Integer.parseInt(this.options.get("weight_levels")), null, false);
        movesHistory.add(bestMove);

        while (!this.isInterrupted()) {
            Move newMove = getRandomMove(this.g, this.d.getNumOfMoves(), Integer.parseInt(this.options.get("weight_levels")), game.getPlayerAMove(), Boolean.parseBoolean(this.options.get("clever")));
            game.setPlayer(PlayerEnum.A, bestMove);
            game.setPlayer(PlayerEnum.B, newMove);
            int gameScore = game.runGame(this.d, Double.parseDouble(this.options.get("epsilon"))).score;
            if (gameScore < 0) {
            } else if (gameScore == 0) {
                if (moveDraws.add(game.getPlayerBMove())) {
                    if (!this.d.getTournament()) {
                        Helper.log("Draw with move %s", game.getPlayerBMove());
                    }
                }
            } else {
                boolean contained = movesHistory.add(newMove);
                if (!contained) {
                    if (!this.d.getTournament()) {
                        Helper.log("Going in circles after %s", game.getPlayerBMove());
                    }
                }
                moveDraws.clear();
                bestMove = newMove;
                this.movePtr.set(bestMove);
                if (!this.d.getTournament()) {
                    Helper.log("New best move %s", newMove.toString());
                }
            }
        }
    }

    @Override
    public Player putDefaultOptions() {
        this.options.put("weight_levels", "10");
        this.options.put("epsilon", String.valueOf(Finals.DEFAULT_EPSILON));
        this.options.put("clever", "false");
        return this;
    }
}