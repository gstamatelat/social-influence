package gr.james.socialinfluence.graph;

import gr.james.socialinfluence.helper.Finals;
import gr.james.socialinfluence.helper.Helper;

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
            Helper.logError(Finals.W_EDGE_WEIGHT_NEGATIVE, weight);
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