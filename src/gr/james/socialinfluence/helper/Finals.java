package gr.james.socialinfluence.helper;

import java.util.Random;

public class Finals {
    /* Constants */
    public static final Random RANDOM = new Random();
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    public static final double DEFAULT_GRAPH_STATE = 0.5;
    public static final String DEFAULT_GRAPH_NAME = "G";
    public static final double DEFAULT_EPSILON = 0.0;

    /* Warnings: these finals must be arguments to Helper.logError() */
    public static final String W_WAITING_PLAYER = "WARNING: Been waiting %d seconds for %s to terminate gracefully.";
    public static final String W_EDGE_WEIGHT_NEGATIVE = "WARNING: Edge weight must be positive. Got %f. No change done.";
    public static final String W_MOVE_EXCEED = "WARNING: Move %s contains more than %d vertices. Slicing to %s. This indicates a mistake in your player.";

    /* Exceptions: these finals must be arguments to GraphException() */
    public static final String E_ILLEGAL_ARGUMENT_NULL = "Argument %s can't be null in Class %s";
    public static final String E_NO_PARAMETER = "%s doesn't have any parameter with name %s.";
    public static final String E_OPTION_NOT_NULL = "Player option value can't be null.";
    public static final String E_BARABASI_STEP = "stepEdges <= initialClique is a constraint";
    public static final String E_MOVE_WEIGHT_NEGATIVE = "A player is trying to add a Vertex on a Move object with a non-positive weight %f but no change was performed. This is caused when .putVertex() is called with a negative weight argument.";
}