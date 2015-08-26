package gr.james.influence.algorithms.scoring.util;

import gr.james.influence.graph.Vertex;

import java.util.Map;

public interface ClosenessHandler {
    double apply(Vertex v, Map<Vertex, Double> distances);
}
