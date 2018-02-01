package gr.james.influence.graph;

import java.util.Objects;

final class GraphTypeImpl implements GraphType {
    private final boolean directed;
    private final boolean weighted;

    GraphTypeImpl(boolean directed, boolean weighted) {
        this.directed = directed;
        this.weighted = weighted;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public boolean isWeighted() {
        return weighted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GraphTypeImpl graphType = (GraphTypeImpl) o;
        return directed == graphType.directed &&
                weighted == graphType.weighted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(directed, weighted);
    }

    @Override
    public String toString() {
        return String.format("GraphType { directed = %b, weighted = %b }", directed, weighted);
    }
}
