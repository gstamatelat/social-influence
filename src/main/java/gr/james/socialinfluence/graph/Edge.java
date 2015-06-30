package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.helper.Finals;

public class Edge {
    private double weight;

    public Edge() {
        this.weight = Finals.DEFAULT_EDGE_WEIGHT;
    }

    public double getWeight() {
        return this.weight;
    }

    public Edge setWeight(double weight) {
        if (weight <= 0) {
            Finals.LOG.warn(Finals.L_EDGE_WEIGHT_NEGATIVE, weight);
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