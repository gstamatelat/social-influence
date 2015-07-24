package gr.james.socialinfluence.graph.algorithms.iterators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.algorithms.PageRank;

/**
 * @deprecated Replaced by {@link GraphStateIterator}
 */
@Deprecated
public class PageRankIterator extends GraphStateIterator<Double> {
    public PageRankIterator(Graph g, double dampingFactor) {
        super(PageRank.execute(g, dampingFactor));
    }
}
