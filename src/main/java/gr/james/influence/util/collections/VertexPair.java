package gr.james.influence.util.collections;

/**
 * <p>A {@link Pair} of {@link V vertices}.</p>
 */
public class VertexPair<V> extends Pair<V> {
    public VertexPair(V source, V target) {
        super(source, target);
    }

    public V getSource() {
        return this.getFirst();
    }

    public V getTarget() {
        return this.getSecond();
    }
}
