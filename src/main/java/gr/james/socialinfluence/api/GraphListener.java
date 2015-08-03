package gr.james.socialinfluence.api;

import gr.james.socialinfluence.graph.Vertex;

public interface GraphListener {
    void vertexAdded(Vertex v);

    void vertexRemoved(Vertex v);
}
