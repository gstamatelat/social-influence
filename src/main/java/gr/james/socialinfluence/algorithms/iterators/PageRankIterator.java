package gr.james.socialinfluence.algorithms.iterators;

import gr.james.socialinfluence.algorithms.scoring.PageRank;
import gr.james.socialinfluence.api.Graph;

/**
 * @deprecated Replaced by {@link GraphStateIterator}
 */
@Deprecated
public class PageRankIterator extends GraphStateIterator<Double> {
    public PageRankIterator(Graph g, double dampingFactor) {
        super(PageRank.execute(g, dampingFactor));
    }
}
