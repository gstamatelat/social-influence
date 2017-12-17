package gr.james.influence.graph;

/**
 * Represents an edge direction, which can be {@link #INBOUND} or {@link #OUTBOUND}.
 */
public enum Direction {
    /**
     * The inbound direction.
     */
    INBOUND,

    /**
     * The outbound direction.
     */
    OUTBOUND;

    /**
     * Returns {@code true} if this {@link Direction} is {@link #INBOUND}, otherwise {@code false}.
     *
     * @return {@code true} if this {@link Direction} is {@link #INBOUND}, otherwise {@code false}
     */
    public boolean isInbound() {
        return (this == INBOUND);
    }

    /**
     * Returns {@code true} if this {@link Direction} is {@link #OUTBOUND}, otherwise {@code false}.
     *
     * @return {@code true} if this {@link Direction} is {@link #OUTBOUND}, otherwise {@code false}
     */
    public boolean isOutbound() {
        return (this == OUTBOUND);
    }
}
