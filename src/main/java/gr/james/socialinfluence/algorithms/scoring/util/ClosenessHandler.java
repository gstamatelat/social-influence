package gr.james.socialinfluence.algorithms.scoring.util;

import gr.james.socialinfluence.graph.Vertex;

import java.util.Map;

public interface ClosenessHandler {
    double apply(Vertex v, Map<Vertex, Double> distances);
}
