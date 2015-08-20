package gr.james.socialinfluence.main;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.GraphState;

import java.io.IOException;

public class Examples {
    public static void main(String[] args) throws IOException {
        GraphState<Integer> state = new GraphState<>();
        state.put(new Vertex(), 1);
        state.put(new Vertex(), 2);
        double max = state.getMax().getWeight();
        double min = state.getMin().getWeight();
        System.out.println(state);
        int ggg = 0;
    }
}
