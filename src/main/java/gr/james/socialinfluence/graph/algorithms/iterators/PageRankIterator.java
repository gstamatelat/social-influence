package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.graph.algorithms.PageRank;
import gr.james.socialinfluence.util.RandomHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @deprecated Replaced by {@link GraphStateIterator}
 */
@Deprecated
public class PageRankIterator implements Iterator<Vertex> {
    // TODO: Consider using PriorityQueue
    private GraphState<Double> nodes;

    public PageRankIterator(Graph g, double dampingFactor) {
        nodes = PageRank.execute(g, dampingFactor);
    }

    @Override
    public boolean hasNext() {
        return this.nodes.size() > 0;
    }

    @Override
    public Vertex next() {
        Double maxPr = 0.0;

        for (Map.Entry<Vertex, Double> e : nodes.entrySet()) {
            if (e.getValue() > maxPr) {
                maxPr = e.getValue();
            }
        }

        ArrayList<Vertex> maxVertices = new ArrayList<>();
        for (Map.Entry<Vertex, Double> e : this.nodes.entrySet()) {
            if (e.getValue().equals(maxPr)) {
                maxVertices.add(e.getKey());
            }
        }

        Vertex maxVertex = maxVertices.get(RandomHelper.getRandom().nextInt(maxVertices.size()));
        this.nodes.remove(maxVertex);
        return maxVertex;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
