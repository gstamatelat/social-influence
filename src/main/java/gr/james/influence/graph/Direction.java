package gr.james.influence.graph;

public enum Direction {
    INBOUND, OUTBOUND;

    public boolean isInbound() {
        return (this == INBOUND);
    }

    public boolean isOutbound() {
        return (this == OUTBOUND);
    }
}
