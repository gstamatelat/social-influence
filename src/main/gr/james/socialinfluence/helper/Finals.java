package gr.james.socialinfluence.helper;

public class Finals {
    /* Default values */
    public static final String DEFAULT_GRAPH_NAME = "G";
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    public static final double DEFAULT_GRAPH_STATE = 0.5;
    public static final double DEFAULT_DEGROOT_PRECISION = 0.0;
    public static final double DEFAULT_PAGERANK_PRECISION = 0.0;
    public static final double DEFAULT_GAME_PRECISION = 1.0e-8;
    public static final boolean DEFAULT_DEGROOT_HISTORY = true;

    /* Warnings: these finals must be arguments to Helper.logError() */
    public static final String W_PLAYER_WAITING = "WARNING: Been waiting %d seconds for %s to terminate gracefully.";
    public static final String W_EDGE_WEIGHT_NEGATIVE = "WARNING: Edge weight must be positive. Got %f. No change done.";
    public static final String W_GAME_MOVE_EXCEED = "WARNING: Move %s contains more than %d vertices. Slicing to %s. This indicates a mistake in your player.";
    public static final String W_GAME_INVALID_VERTEX = "WARNING: A player requested a move vertex that doesn't belong in the graph. This indicates a mistake in the engine.";
    public static final String W_GAME_EMPTY_MOVE = "WARNING: A player submitted an empty move or didn't terminate before submitting a move.";
    public static final String W_DEGROOT_PERIODIC = "WARNING: DeGroot periodicity on {%s}.";

    /* Exceptions: these finals must be arguments to GraphException(). Format: E_CLASSNAME_IDENTIFIER. */
    public static final String E_PLAYER_NO_PARAMETER = "%s doesn't have any parameter with name %s.";
    public static final String E_PLAYER_OPTION_NULL = "Player option value can't be null.";
    public static final String E_PLAYER_EXCEPTION = "Player %s triggered exception <%s> on graph %s and definition %s";

    public static final String E_RANDOM_HELPER_INIT = "Global Random instance has been already initialized";

    public static final String E_MOVEPOINTER_SET_NULL = "Cannot submit a null move.";

    public static final String E_BARABASI_STEP = "stepEdges <= initialClique is a constraint";

    public static final String E_MOVE_WEIGHT_NEGATIVE = "A player is trying to add a Vertex on a Move object with a non-positive weight %f. This is caused when .putVertex() is called with a negative weight argument.";

    public static final String E_GRAPH_VERTEX_BOUND = "Trying to add a vertex that already belongs to another graph. Remove it first.";
    public static final String E_GRAPH_EDGE_DIFFERENT = "The source and target of an edge must both belong to the graph you are trying to add it.";
    public static final String E_GRAPH_INDEX_OUT_OF_BOUNDS = "Vertex index must be between 0 (inclusive) and getVerticesCount() (exclusive). Got %d.";

    public static final String E_PAIR_NULL = "Pair cannot contain null values";
}