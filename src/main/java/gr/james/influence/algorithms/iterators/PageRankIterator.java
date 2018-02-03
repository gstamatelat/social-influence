package gr.james.influence.algorithms.iterators;

import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.graph.DirectedGraph;

public class PageRankIterator<V> extends GraphStateIterator<V, Double> {
    public PageRankIterator(DirectedGraph<V, ?> g, double dampingFactor) {
        super(PageRank.execute(g, dampingFactor));
    }

    public PageRankIterator(DirectedGraph<V, ?> g, double dampingFactor, double epsilon) {
        super(PageRank.execute(g, dampingFactor, epsilon));
    }
}
