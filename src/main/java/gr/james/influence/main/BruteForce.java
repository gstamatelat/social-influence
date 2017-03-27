package gr.james.influence.main;

import gr.james.influence.algorithms.generators.random.RandomGenerator;
import gr.james.influence.api.Graph;
import gr.james.influence.game.Game;
import gr.james.influence.game.GameDefinition;
import gr.james.influence.game.GameResult;
import gr.james.influence.game.Move;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BruteForce {
    private static final int SCALE = 2;

    public static void main(String[] args) {
        /* Parameters */
        //Graph g = new TwoWheelsGenerator(5).generate();
        Graph g = new RandomGenerator(8, 0.2, false).generate();
        GraphUtils.connect(g);

        /* The initial move */
        //Move m = new Move(g.getVertexFromIndex(4), g.getVertexFromIndex(9), g.getVertexFromIndex(10));
        Move m = new Move();
        for (Vertex v : g) {
            m.putVertex(v, 1.0);
        }

        /* Run */
        optimize(g, m, -SCALE, SCALE);
    }

    public static void optimize(Graph g, Move q, int min, int max) {
        Move best = q.deepCopy();

        GameDefinition d = new GameDefinition(q.getVerticesCount(), (double) q.getVerticesCount(), 0, 0.0);

        int[] c = IntStream.generate(() -> min).limit(q.getVerticesCount()).toArray();
        c[0]--;

        //System.out.printf("Initial move: %s%n", best);

        while ((c = advanceNumber(c, min, max)) != null) {
            Move m = best.deepCopy();
            int i = 0;
            for (Vertex v : m) {
                m.putVertex(v, Math.max(m.getWeight(v) + c[i++], 1));
            }

            GameResult r = Game.runMoves(g, d, best, m);
            if (r.score > 0) {
                System.out.printf("Better move: %s on %s%n", m, Arrays.toString(c));
                best = m;
            } else if (r.score == 0) {
                System.out.printf("Drawn move: %s on %s%n", m, Arrays.toString(c));
            }
        }

        boolean done = false;
        for (Vertex v : best) {
            best.putVertex(v, best.getWeight(v) * SCALE);
            if (best.getWeight(v) > 1000000) {
                done = true;
            }
        }
        if (!done) {
            optimize(g, best, min, max);
        }
    }

    public static int[] advanceNumber(int[] n, int min, int max) {
        int[] t = Arrays.copyOf(n, n.length);

        boolean carry = true;
        for (int i = 0; i < t.length; i++) {
            if (carry) {
                t[i]++;
                carry = false;
            }
            if (t[i] > max) {
                t[i] = min;
                carry = true;
            }
        }

        if (!carry) {
            return t;
        } else {
            return null;
        }
    }
}
