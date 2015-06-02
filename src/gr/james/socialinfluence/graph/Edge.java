package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

public class Edge {
    Vertex source;
    Vertex target;
    Graph parentGraph;
    double weight;

    Edge() {
        this.weight = Finals.DEFAULT_EDGE_WEIGHT;
    }

    public Edge(double weight) {
        this.setWeight(weight);
    }

    public double getWeight() {
        return this.weight;
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getTarget() {
        return this.target;
    }

    public Graph remove() {
        return this.parentGraph.removeEdge(this);
    }

    public Edge setWeight(double weight) {
        if (weight <= 0) {
            Helper.logError(String.format(Finals.S_EDGE_WEIGHT_NEGATIVE, weight));
        } else {
            this.weight = weight;
        }
        return this;
    }

    @Override
    public int hashCode() {
        return 5 * source.hashCode() + 7 * target.hashCode();
    }

    /**
     * <p>Compares this Edge to the specified object. The result is {@code true} if the edges have the same {@code
     * source} and the same {@code target} (that is, with the same ids); {@code weight} does not play any role in the
     * result.</p>
     *
     * @param obj The object to compare this {@code Edge} against
     * @return {@code true} if the argument represents an {@code Edge} equivalent to this edge, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Edge))
            return false;

        Edge rhs = (Edge) obj;

        if (!rhs.source.equals(this.source)) {
            return false;
        }
        if (!rhs.target.equals(this.target)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return source.toString() + " -> " + target.toString() + " [" + weight + "]";
    }
}