package gr.james.influence.algorithms.iterators;

import gr.james.influence.algorithms.scoring.PageRank;
import gr.james.influence.api.Graph;

public class PageRankIterator extends GraphStateIterator<Double> {
    public PageRankIterator(Graph g, double dampingFactor) {
        super(PageRank.execute(g, dampingFactor));
    }

    public PageRankIterator(Graph g, double dampingFactor, double epsilon) {
        super(PageRank.execute(g, dampingFactor, epsilon));
    }
}
