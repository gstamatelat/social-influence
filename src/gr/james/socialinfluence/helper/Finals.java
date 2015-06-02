package gr.james.socialinfluence.helper;

import java.util.Random;

public class Finals {
    public static final Random RANDOM = new Random();
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    public static final double DEFAULT_GRAPH_STATE = 0.5;
    public static final String DEFAULT_GRAPH_NAME = "G";
    public static final double DEFAULT_EPSILON = 0.0;

    public static final String S_WAITING_PLAYER = "WARNING: Been waiting %d seconds for %s to terminate gracefully.";
    public static final String S_NO_PARAMETER = "ERROR: %s doesn't have any parameter with name %s.";
    public static final String S_OPTION_NOT_NULL = "ERROR: Option value can't be null.";
    public static final String S_EDGE_WEIGHT_NEGATIVE = "WARNING: Edge weight must be positive. Got %f. No change done.";
    public static final String S_MOVE_VERTEX_NULL = "WARNING: A player is trying to add a null Vertex on a Move object but no change was performed. This is caused when .putVertex() is called with a null argument.";
    public static final String S_MOVE_WEIGHT_NEGATIVE = "WARNING: A player is trying to add a Vertex on a Move object with a non-positive weight %f but no change was performed. This is caused when .putVertex() is called with a negative weight argument.";
    public static final String S_MOVE_EXCEED = "WARNING: Move %s contains more than %d vertices. Slicing to %s. This indicates a mistake in your player.";
}