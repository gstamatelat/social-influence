package gr.james.socialinfluence.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finals {
    /* Root logger */
    public static final Logger LOG = LoggerFactory.getLogger("");

    /* Default values */
    public static final String DEFAULT_GRAPH_NAME = "G";
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    public static final double DEFAULT_GRAPH_STATE = 0.5;
    public static final double DEFAULT_DEGROOT_PRECISION = 0.0;
    public static final double DEFAULT_PAGERANK_PRECISION = 0.0;
    public static final double DEFAULT_GAME_PRECISION = 1.0e-8;
    public static final boolean DEFAULT_DEGROOT_HISTORY = true;

    /* Warnings and other logging messages */
    public static final String L_PLAYER_WAITING = "Been waiting {} seconds for {} to terminate gracefully.";
    public static final String L_EDGE_WEIGHT_NEGATIVE = "Edge weight must be positive. Got {}. No change done.";
    public static final String L_GAME_MOVE_EXCEED = "Move {} contains more than {} vertices. Slicing to {}. This indicates a mistake in your player.";
    public static final String L_GAME_INVALID_VERTEX = "A player requested a move vertex that doesn't belong in the graph. This indicates a mistake in the engine.";
    public static final String L_GAME_EMPTY_MOVE = "A player submitted an empty move or didn't terminate before submitting a move.";
    public static final String L_GAME_EMPTY_MOVES = "Both moves were empty. By definition, draw.";
    public static final String L_DEGROOT_PERIODIC = "DeGroot periodicity on {}.";
    public static final String L_PLAYER_EXCEPTION = "Player {} triggered exception <{}> on graph {} and definition {}";
    public static final String L_RANDOM_SEED = "Initialized new global random with seed {}";
    public static final String L_RANDOM_ERROR = "Global Random instance has been already initialized";

    /* Exceptions: these finals must be arguments to GraphException(). Format: E_CLASSNAME_IDENTIFIER. */
    public static final String E_PLAYER_NO_PARAMETER = "%s doesn't have any parameter with name %s.";
    public static final String E_PLAYER_OPTION_NULL = "Player option value can't be null.";

    public static final String E_MOVEPOINTER_SET_NULL = "Cannot submit a null move.";

    public static final String E_BARABASI_STEP = "stepEdges <= initialClique is a constraint";

    public static final String E_MOVE_WEIGHT_NEGATIVE = "A player is trying to add a Vertex on a Move object with a non-positive weight %f. This is caused when .putVertex() is called with a negative weight argument.";

    public static final String E_GRAPH_EDGE_DIFFERENT = "The source and target of an edge must both belong to the graph you are trying to add it.";
    public static final String E_GRAPH_INDEX_OUT_OF_BOUNDS = "Vertex index must be between 0 (inclusive) and getVerticesCount() (exclusive). Got %d.";
    public static final String E_GRAPH_VERTEX_NOT_CONTAINED = "The vertex specified in %s doesn't belong in the graph.";

    public static final String E_PAIR_NULL = "Pair cannot contain null values";

    public static final String E_HELPER_INSTANTIATE = "Cannot instantiate object of type %s";
}