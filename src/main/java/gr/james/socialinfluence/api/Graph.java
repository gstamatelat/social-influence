package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Edge;
import gr.james.socialinfluence.graph.Vertex;

import java.util.Collection;
import java.util.Set;

public interface Graph extends ImmutableGraph {
    Graph setMeta(String key, String value);

    Graph clearMeta();

    Vertex addVertex();

    Vertex addVertex(Vertex v);

    Set<Vertex> addVertices(int count);

    Graph removeVertex(Vertex v);

    Graph removeVertices(Collection<Vertex> vertices);

    Graph clear();

    Edge addEdge(Vertex source, Vertex target);

    Set<Edge> addEdge(Vertex source, Vertex target, boolean undirected);

    Graph removeEdge(Vertex source, Vertex target);
}
