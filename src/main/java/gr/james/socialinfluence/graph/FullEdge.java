package gr.james.socialinfluence.graph;

public class FullEdge {
    private Vertex source;
    private Vertex target;
    private Edge edge;

    public FullEdge(Vertex source, Vertex target, Edge edge) {
        // TODO: throw exception is some field is null
        this.source = source;
        this.target = target;
        this.edge = edge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FullEdge fullEdge = (FullEdge) o;

        if (!source.equals(fullEdge.source)) return false;
        return target.equals(fullEdge.target);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public Edge getEdge() {
        return edge;
    }
}