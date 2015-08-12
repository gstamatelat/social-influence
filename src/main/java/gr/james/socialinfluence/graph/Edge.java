package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.util.Finals;
import gr.james.socialinfluence.util.exceptions.WeightNonPositiveException;

/**
 * <p>Represents a graph edge, which in its primitive form only contains a weight (double) value. An edge's weight can
 * only be positive.</p>
 */
public class Edge {
    private double weight;

    /**
     * <p>Construct a new {@code Edge} with {@link Finals#DEFAULT_EDGE_WEIGHT} weight.</p>
     */
    public Edge() {
        this.weight = Finals.DEFAULT_EDGE_WEIGHT;
    }

    /**
     * <p>Gets the weight of this edge.</p>
     *
     * @return the weight of this edge
     * @see #setWeight(double)
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * <p>Set the weight value of this edge.</p>
     *
     * @param weight the new weight of this edge
     * @return the current {@code Edge} instance to fulfill the builder pattern
     * @throws WeightNonPositiveException if weight is negative or zero
     * @see #getWeight()
     * @deprecated This method will be removed because it violates the immutability of {@code Edge}
     */
    @Deprecated
    public Edge setWeight(double weight) {
        if (weight <= 0) {
            throw new WeightNonPositiveException(Finals.E_EDGE_WEIGHT_NEGATIVE, weight);
        } else {
            this.weight = weight;
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("%.2f", this.weight);
    }
}
