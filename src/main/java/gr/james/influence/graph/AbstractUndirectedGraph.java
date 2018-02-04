package gr.james.influence.graph;

abstract class AbstractUndirectedGraph<V, E> implements UndirectedGraph<V, E> {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("UndirectedGraph(%d) {%n", this.vertexCount()));
        for (UndirectedEdge<V, E> e : this.edges()) {
            if (e.value() == null) {
                sb.append(String.format("  %s -- %s [%.2f]%n", e.v(), e.w(), e.weight()));
            } else {
                sb.append(String.format("  %s -- %s (%s) [%.2f]%n", e.v(), e.w(), e.value(), e.weight()));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
