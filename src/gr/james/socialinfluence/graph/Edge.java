package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.graph.collections.VertexPair;
import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

public class Edge extends VertexPair {
    double weight;

    Edge(Vertex source, Vertex target) {
        super(source, target);
        this.weight = Finals.DEFAULT_EDGE_WEIGHT;
    }

    public Vertex getSource() {
        return this.getFirst();
    }

    public Vertex getTarget() {
        return this.getSecond();
    }

    public double getWeight() {
        return this.weight;
    }

    public Edge setWeight(double weight) {
        if (weight <= 0) {
            Helper.logError(Finals.W_EDGE_WEIGHT_NEGATIVE, weight);
        } else {
            this.weight = weight;
        }
        return this;
    }

    /**
     * <p>Compares this Edge to the specified object. The result is {@code true} if the edges have the same {@code
     * source} and the same {@code target} (that is, with the same ids); {@code weight} does not play any role in the
     * result.</p>
     *
     * @param o The object to compare this {@code Edge} against
     * @return {@code true} if the argument represents an {@code Edge} equivalent to this edge, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return getSource().equals(edge.getSource()) && getTarget().equals(edge.getTarget());
    }

    @Override
    public int hashCode() {
        return 31 * getSource().hashCode() + getTarget().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s -> %s [%f]", this.getSource(), this.getTarget(), this.weight);
    }
}