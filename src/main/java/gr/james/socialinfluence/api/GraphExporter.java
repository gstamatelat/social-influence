package gr.james.socialinfluence.api;

import java.io.PrintStream;

@FunctionalInterface
public interface GraphExporter {
    void to(Graph g, PrintStream out);
}
