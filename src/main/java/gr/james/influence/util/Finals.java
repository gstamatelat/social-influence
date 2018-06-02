package gr.james.influence.util;

import gr.james.influence.graph.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Finals {
    /* Root logger */
    public static final Logger LOG = LoggerFactory.getLogger("gr.james.influence");

    /* Constants */
    public static final String TYPE_META = "type";
    public static final Charset IO_ENCODING = StandardCharsets.UTF_8;

    /* Default values */
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;
    public static final double DEFAULT_DEGROOT_PRECISION = 0.0;
    public static final double DEFAULT_GAME_OPINIONS = 0.5;
    public static final double DEFAULT_PAGERANK_PRECISION = 0.0;
    public static final String DEFAULT_INDENT = "  ";
    public static final Direction DEFAULT_DIRECTION = Direction.INBOUND;
    //public static final GraphFactory<Integer, Object> DEFAULT_GRAPH_FACTORY = SimpleGraph.graphFactory;

    /* Logging messages */
    public static final String L_GAME_MOVE_EXCEED = "Move {} contains more than {} vertices. Slicing to {}. This indicates a mistake in your player.";
    public static final String L_PERIODIC = "Periodicity on {} with maxSize = {} and history ={}{}{}";

    /* Exceptions: these finals must be arguments to GraphException(). Format: E_CLASSNAME_IDENTIFIER. */
    public static final String E_MOVEPOINTER_SET_NULL = "Cannot submit a null move.";

    public static final String E_BARABASI_STEP = "stepEdges <= initialClique is a constraint";
}
