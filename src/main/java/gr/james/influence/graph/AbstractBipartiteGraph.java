package gr.james.influence.graph;

abstract class AbstractBipartiteGraph<V, E> implements BipartiteGraph<V, E> {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("BipartiteGraph(%d,%d+%d) {%n",
                this.vertexCount(), this.vertexASet().size(), this.vertexBSet().size()));
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
