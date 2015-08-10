package gr.james.socialinfluence.game.players;

import gr.james.socialinfluence.algorithms.distance.Dijkstra;
import gr.james.socialinfluence.algorithms.iterators.PageRankIterator;
import gr.james.socialinfluence.game.GameDefinition;
import gr.james.socialinfluence.game.Move;
import gr.james.socialinfluence.game.MovePointer;
import gr.james.socialinfluence.game.Player;
import gr.james.socialinfluence.graph.ImmutableGraph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class GreedyPlayer extends Player {
    public static Move getMinimum(HashMap<Move, Double> treeMoves) {
        Double minSumMove = Double.POSITIVE_INFINITY;
        Move minMove = null;
        for (Map.Entry<Move, Double> e : treeMoves.entrySet()) {
            if (e.getValue() < minSumMove) {
                minSumMove = e.getValue();
                minMove = e.getKey();
            }
        }
        return minMove;
    }

    public static void updateVector(HashMap<Vertex[], Double> distanceMap, HashMap<Vertex, Double> vector, Vertex move) {
        for (Map.Entry<Vertex[], Double> e : distanceMap.entrySet()) {
            Vertex[] tt = e.getKey();
            if (tt[0].equals(move)) {
                vector.put(tt[1], vector.get(tt[1]) * (1 - e.getValue()));
            }
        }
    }

    public static Double vectorSum(HashMap<Vertex, Double> v) {
        Double sum = 0.0;
        for (Map.Entry<Vertex, Double> e : v.entrySet()) {
            sum += e.getValue();
        }
        return sum;
    }

    @Override
    public void suggestMove(ImmutableGraph g, GameDefinition d, MovePointer movePtr) {
        /* Here be distanceMap and vector */
        HashMap<Vertex[], Double> distanceMap = new HashMap<>();
        HashMap<Vertex, Double> vector = new HashMap<>();

        /* Fill the distanceMap */
        // TODO: Replace this snippet with FloydWarshall method, but care, this map has (t,s) rather than (s,t)
        for (Vertex v : g) {
            Map<Vertex, Double> temp = Dijkstra.execute(g, v);
            for (Map.Entry<Vertex, Double> e : temp.entrySet()) {
                distanceMap.put(new Vertex[]{e.getKey(), v}, e.getValue());
            }
        }
        // TODO: Replace up to here
        for (Map.Entry<Vertex[], Double> e : distanceMap.entrySet()) {
            e.setValue(1 / Math.exp(e.getValue()));
        }

        HashMap<Move, Double> treeMoves = new HashMap<>();

        PageRankIterator pri = new PageRankIterator(g, 0.0);
        while (pri.hasNext() && !this.isInterrupted()) {
            Vertex firstGuess = pri.next().getObject();

            /* Initialize the vector */
            vector.clear();
            for (Vertex v : g) {
                vector.put(v, 1.0);
            }

            /* Insert the first node to the move and update the vector */
            Move m = new Move();
            m.putVertex(firstGuess, 1.0);
            updateVector(distanceMap, vector, firstGuess);

            /* Simulation loop */
            while (m.getVerticesCount() < d.getActions()) {
                HashMap<Vertex, Double> sumMap = new HashMap<>();
                for (Vertex v : g) {
                    HashMap<Vertex, Double> tmpVector = new HashMap<>();
                    for (Map.Entry<Vertex, Double> e : vector.entrySet()) {
                        tmpVector.put(e.getKey(), e.getValue());
                    }
                    updateVector(distanceMap, tmpVector, v);
                    sumMap.put(v, vectorSum(tmpVector));
                }
                Double minSum = Double.POSITIVE_INFINITY;
                Vertex minNode = null;
                for (Map.Entry<Vertex, Double> e : sumMap.entrySet()) {
                    if (e.getValue() < minSum) {
                        minNode = e.getKey();
                        minSum = e.getValue();
                    }
                }
                m.putVertex(minNode, 1.0);
                updateVector(distanceMap, vector, minNode);
            }

            /* Insert this move to the list */
            m.normalizeWeights(d.getBudget()); // TODO: Can this line safely be removed?
            treeMoves.put(m, vectorSum(vector));

            /* Find the best move, aka the one with min vector sum */
            Move minMove = getMinimum(treeMoves);
            movePtr.submit(minMove);

            /* This helps when computation takes a long time */
            log.info("{} : {}", firstGuess, minMove);
        }
    }
}
