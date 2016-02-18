package gr.james.influence.game.players;

import gr.james.influence.algorithms.iterators.RandomSurferIterator;
import gr.james.influence.algorithms.iterators.RandomVertexIterator;
import gr.james.influence.api.Graph;
import gr.james.influence.game.*;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.RandomHelper;

import java.util.HashSet;

/**
 * <p>Implements a simple brute-force player.</p>
 * <p>Options:</p>
 * <ul>
 * <li>weightLevels: Default value is 10.</li>
 * <li>epsilon: Default value is 0.0.</li>
 * <li>clever: Instead of picking new moves completely at random, use a mutation, a move that is relatively close to
 * the previous move. Enabling this option should theoretically result in better results most of the time but may end
 * up in a local maximum. This is disabled by default.</li>
 * </ul>
 */
public class MasterBruteForcePlayer extends Player {
    private boolean clever;
    private int weightLevels;
    private double epsilon;

    public MasterBruteForcePlayer(boolean clever, int weightLevels, double epsilon) {
        this.clever = clever;
        this.weightLevels = weightLevels;
        this.epsilon = epsilon;
    }

    public MasterBruteForcePlayer() {
        this.clever = false;
        this.weightLevels = 10;
        this.epsilon = 0.0;
    }

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
            moves.putVertex(it.next(), RandomHelper.getRandom().nextInt(weightLevels) + 1);
        }
        return moves;
    }

    public static Move getRandomMoveWithMutation(Graph g, int numOfMoves, int weightLevels, Move lastMove) {
        double jump_probability = 0.2; // TODO: Make it an option, or even better adaptive since it should depend on the diameter of the graph

        Move moves = new Move();
        // TODO: This loop is problematic. There is a chance that 2 nodes surf to the same node and thus create a move with less than numOfMoves nodes.
        // TODO: If this happens, it will never increase the nodes again
        for (Vertex mp : lastMove) {
            RandomSurferIterator randomSurfer = new RandomSurferIterator(g, 0.0, mp);
            while (RandomHelper.getRandom().nextDouble() < jump_probability) {
                mp = randomSurfer.next();
            }
            moves.putVertex(mp, RandomHelper.getRandom().nextInt(weightLevels) + 1);
        }
        return moves;
    }

    @Override
    public void suggestMove(Graph g, GameDefinition d, MovePointer movePtr) {
        Graph mg = GraphUtils.deepCopy(g);

        HashSet<Move> movesHistory = new HashSet<>();
        HashSet<Move> moveDraws = new HashSet<>();

        Move bestMove = getRandomMove(g, d.getActions(), weightLevels, null, false);
        movesHistory.add(bestMove);

        while (!this.isInterrupted()) {
            Move newMove = getRandomMove(g, d.getActions(), weightLevels, bestMove, clever);
            int gameScore = Game.runMoves(mg, d, bestMove, newMove).score;
            if (gameScore == 0) {
                if (moveDraws.add(newMove)) {
                    log.debug("Draw with move {}", newMove);
                }
            } else if (gameScore > 0) {
                boolean contained = movesHistory.add(newMove);
                if (!contained) {
                    log.debug("Going in circles after {}", newMove);
                }
                moveDraws.clear();
                bestMove = newMove;
                movePtr.submit(bestMove);
                log.info("New best move {}", newMove.toString());
            }
        }
    }
}
