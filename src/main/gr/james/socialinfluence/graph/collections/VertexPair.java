package gr.james.socialinfluence.graph.collections;

import gr.james.socialinfluence.graph.Vertex;

/**
 * <p>A {@link Pair} of {@link Vertex vertices}.</p>
 */
public class VertexPair extends Pair<Vertex> {
    public VertexPair(Vertex source, Vertex target) {
        super(source, target);
    }
}