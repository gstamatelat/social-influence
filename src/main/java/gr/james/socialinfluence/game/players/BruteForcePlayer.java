package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.algorithms.iterators.RandomSurferIterator;
import gr.james.socialinfluence.algorithms.iterators.RandomVertexIterator;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.game.*;
import gr.james.socialinfluence.graph.GraphUtils;
import gr.james.socialinfluence.graph.ImmutableGraph;
import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.RandomHelper;

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
    public static Move getRandomMove(ImmutableGraph g, int numOfMoves, int weightLevels, Move lastMove, boolean clever) {
        if (!clever) {
            return getRandomMoveWithoutMutation(g, numOfMoves, weightLevels);
        } else {
            return getRandomMoveWithMutation(g, numOfMoves, weightLevels, lastMove);
        }
    }

    public static Move getRandomMoveWithoutMutation(ImmutableGraph g, int numOfMoves, int weightLevels) {
        Move moves = new Move();
        RandomVertexIterator it = new RandomVertexIterator(g);
        while (moves.getVerticesCount() < numOfMoves) {
            // TODO: Something must be done to allow selecting less than numOfMoves vertices.
            moves.putVertex(it.next(), RandomHelper.getRandom().nextInt(weightLevels) + 1);
        }
        return moves;
    }

    public static Move getRandomMoveWithMutation(ImmutableGraph g, int numOfMoves, int weightLevels, Move lastMove) {
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
    public Player putDefaultOptions() {
        this.options.put("weight_levels", "10");
        this.options.put("epsilon", "0.0");
        this.options.put("clever", "false");
        return this;
    }

    @Override
    public void suggestMove(ImmutableGraph g, GameDefinition d, MovePointer movePtr) {
        Graph mg = GraphUtils.deepCopy(MemoryGraph.class, g); // TODO: Sure this MemoryGraph thing is OK?
        Game game = new Game(mg);

        HashSet<Move> movesHistory = new HashSet<>();
        HashSet<Move> moveDraws = new HashSet<>();

        Move bestMove = getRandomMove(g, d.getActions(), Integer.parseInt(this.options.get("weight_levels")), null, false);
        movesHistory.add(bestMove);

        while (!this.isInterrupted()) {
            Move newMove = getRandomMove(g, d.getActions(), Integer.parseInt(this.options.get("weight_levels")), game.getPlayerAMove(), Boolean.parseBoolean(this.options.get("clever")));
            game.setPlayer(PlayerEnum.A, bestMove);
            game.setPlayer(PlayerEnum.B, newMove);
            int gameScore = game.runGame(d, Double.parseDouble(this.options.get("epsilon"))).score;
            if (gameScore == 0) {
                if (moveDraws.add(game.getPlayerBMove())) {
                    log.info("Draw with move {}", game.getPlayerBMove());
                }
            } else if (gameScore > 0) {
                boolean contained = movesHistory.add(newMove);
                if (!contained) {
                    log.info("Going in circles after {}", game.getPlayerBMove());
                }
                moveDraws.clear();
                bestMove = newMove;
                movePtr.submit(bestMove);
                log.info("New best move {}", newMove.toString());
            }
        }
    }
}
