package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.VertexPair;

import java.util.Map;
import java.util.Set;

public interface ImmutableGraph {
    String getMeta(String key);

    String getGraphType();

    boolean containsVertex(Vertex v);

    boolean containsEdge(Vertex source, Vertex target);

    <T extends Graph> Graph deepCopy(Class<T> type);

    <T extends Graph> Graph deepCopy(Class<T> type, Set<Vertex> includeOnly);

    Vertex getVertexFromIndex(int index);

    Vertex getRandomVertex();

    Set<Vertex> getStubbornVertices();

    Map<VertexPair, Edge> getEdges();

    int getEdgesCount();

    Map<Vertex, Edge> getOutEdges(Vertex v);

    Map<Vertex, Edge> getInEdges(Vertex v);

    double getOutWeightSum(Vertex v);

    double getInWeightSum(Vertex v);

    int getOutDegree(Vertex v);

    int getInDegree(Vertex v);

    boolean isUndirected();

    Set<Vertex> getVertices();

    int getVerticesCount();

    Vertex getRandomOutEdge(Vertex from, boolean weighted);

    double getDiameter();
}
