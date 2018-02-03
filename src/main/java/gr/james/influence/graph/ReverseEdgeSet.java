package gr.james.influence.graph;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

final class ReverseEdgeSet<V, E> extends AbstractSet<DirectedEdge<V, E>> {
    private final Set<DirectedEdge<V, E>> edges;

    ReverseEdgeSet(Set<DirectedEdge<V, E>> edges) {
        this.edges = edges;
    }

    @Override
    public Iterator<DirectedEdge<V, E>> iterator() {
        return new Iterator<DirectedEdge<V, E>>() {
            final Iterator<DirectedEdge<V, E>> it = edges.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public DirectedEdge<V, E> next() {
                return it.next().reverse();
            }
        };
    }

    @Override
    public int size() {
        return edges.size();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof DirectedEdge)) {
            return false;
        }
        DirectedEdge e = (DirectedEdge) o;
        return edges.contains(e.reverse());
    }
}
