package gr.james.influence.util;

import gr.james.influence.api.GraphFactory;
import gr.james.influence.graph.MemoryGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Finals {
    /* Root logger */
    public static final Logger LOG = LoggerFactory.getLogger("gr.james.influence");

    /* Default values */
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    public static final double DEFAULT_DEGROOT_PRECISION = 0.0;
    public static final double DEFAULT_GAME_OPINIONS = 0.5;
    public static final double DEFAULT_PAGERANK_PRECISION = 0.0;
    public static final double DEFAULT_GAME_PRECISION = 0.0;
    public static final GraphFactory<MemoryGraph> DEFAULT_GRAPH_FACTORY = MemoryGraph::new;

    /* Constants */
    public static final String TYPE_META = "type";
    public static final Charset IO_ENCODING = StandardCharsets.UTF_8;

    /* Logging messages */
    public static final String L_PLAYER_WAITING = "Been waiting {} seconds for {} to terminate gracefully.";
    public static final String L_GAME_MOVE_EXCEED = "Move {} contains more than {} vertices. Slicing to {}. This indicates a mistake in your player.";
    public static final String L_GAME_EMPTY_MOVE = "A player submitted an empty move or didn't terminate before submitting a move.";
    public static final String L_GAME_EMPTY_MOVES = "Identical moves. By definition, draw.";
    public static final String L_PERIODIC = "Periodicity on {} with maxSize = {}";
    public static final String L_PLAYER_EXCEPTION = "Player {} triggered exception on graph {} with definition {}\n{}";
    public static final String L_EDGES_IMPORT_MERGE = "The .edges file contains parallel edges [{} -> {}]; duplicates have been merged.";

    /* Exceptions: these finals must be arguments to GraphException(). Format: E_CLASSNAME_IDENTIFIER. */
    public static final String E_EDGE_WEIGHT_NEGATIVE = "Edge weight must be positive. Got %f.";

    public static final String E_MOVEPOINTER_SET_NULL = "Cannot submit a null move.";

    public static final String E_BARABASI_STEP = "stepEdges <= initialClique is a constraint";

    public static final String E_MOVE_WEIGHT_NEGATIVE = "A player is trying to add a Vertex on a Move object with a non-positive weight %f. This is caused when .putVertex() is called with a negative weight argument.";

    public static final String E_GRAPH_VERTEX_NOT_CONTAINED = "The vertex specified in %s doesn't belong in the graph.";

    public static final String E_IMMUTABLE_GRAPH = "Cannot modify a graph of type ImmutableGraph";

    public static final String E_EDGES_IMPORT = "A line in an .edges format file must only contain three records";
}
