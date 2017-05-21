package gr.james.influence.graph;

import gr.james.influence.api.GraphEdge;

public class MemoryGraphEdge implements GraphEdge<Vertex, Edge> {
    private Edge edge;
    private Vertex source;
    private Vertex target;
    private double weight;

    public MemoryGraphEdge(Edge edge, Vertex source, Vertex target, double weight) {
        this.edge = edge;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public Edge getEdge() {
        return edge;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }
}
