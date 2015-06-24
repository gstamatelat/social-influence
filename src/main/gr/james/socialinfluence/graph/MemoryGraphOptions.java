package gr.james.socialinfluence.graph;

public enum MemoryGraphOptions {
    /**
     * <p>Use a {@link java.util.LinkedHashSet} as container for the vertices instead of a {@link java.util.HashSet}. A
     * {@code LinkedHashSet} has slightly slower add() but faster next(). This setting has an effect on the iterator of
     * {@link MemoryGraph#getVertices()}. {@code HashSet} elements don't have any particular order while {@code LinkedHashSet}
     * maintains insertion order of elements. Insertion order of elements is identical with vertex {@code index}
     * order.</p>
     */
    VERTEX_USE_LINKED_HASH_SET,

    /**
     * <p>Use a {@link java.util.LinkedHashSet} as container for the edges instead of a {@link java.util.HashSet}. A
     * {@code LinkedHashSet} has slightly slower add() but faster next(). This setting has an effect on the iterator of
     * {@link MemoryGraph#getEdges()}. {@code HashSet} elements don't have any particular order while {@code LinkedHashSet}
     * maintains insertion order of elements.</p>
     */
    EDGE_USE_LINKED_HASH_SET
}