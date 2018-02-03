package gr.james.influence.graph;

abstract class AbstractDirectedGraph<V, E> implements DirectedGraph<V, E> {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("DirectedGraph(%d) {%n", this.vertexCount()));
        for (DirectedEdge<V, E> e : this.edges()) {
            if (e.value() == null) {
                sb.append(String.format("  %s -> %s [%.2f]%n", e.source(), e.target(), e.weight()));
            } else {
                sb.append(String.format("  %s -> %s (%s) [%.2f]%n", e.source(), e.target(), e.value(), e.weight()));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
