package gr.james.socialinfluence.graph.collections;

import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Finals;

/**
 * <p>Represents a pair of {@link Vertex vertices}. This class is immutable and implements {@link #equals(Object)} and
 * {@link #hashCode()}. It can be safely used inside collections.</p>
 */
public class VertexPair {
    public Vertex source;
    public Vertex target;

    public VertexPair(Vertex source, Vertex target) {
        if (source == null) {
            throw new IllegalArgumentException(String.format(Finals.E_ARGUMENT_NULL, "source", this.getClass().getSimpleName()));
        }
        if (target == null) {
            throw new IllegalArgumentException(String.format(Finals.E_ARGUMENT_NULL, "target", this.getClass().getSimpleName()));
        }
        this.source = source;
        this.target = target;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexPair that = (VertexPair) o;

        return source.equals(that.source) && target.equals(that.target);

    }

    @Override
    public int hashCode() {
        int result = 31 * source.hashCode() + target.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String format = "VertexPair{source=%s,target=%s}';";
        return String.format(format, this.source, this.target);
    }
}