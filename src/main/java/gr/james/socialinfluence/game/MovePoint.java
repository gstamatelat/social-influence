package gr.james.socialinfluence.game;

import gr.james.socialinfluence.graph.Vertex;

/**
 * <p>Represents a move point, which consists of a vertex and a weight.
 * This class is mostly used as a structure.</p>
 */
@Deprecated
public class MovePoint implements Comparable {
    public Vertex vertex;
    public double weight;

    @Override
    public String toString() {
        return this.vertex.toString() + "=" + this.weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MovePoint other = (MovePoint) obj;
        return this.vertex.equals(other.vertex) && (this.weight == other.weight);
    }

    @Override
    public int hashCode() {
        return this.vertex.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        MovePoint rhs = (MovePoint) o;
        int vertexCompare = this.vertex.compareTo(rhs.vertex);
        if (vertexCompare != 0) {
            return vertexCompare;
        } else {
            return Double.compare(this.weight, rhs.weight);
        }
    }
}
