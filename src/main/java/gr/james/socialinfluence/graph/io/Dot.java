package gr.james.socialinfluence.graph.io;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphExporter;
import gr.james.socialinfluence.api.GraphImporter;
import gr.james.socialinfluence.graph.FullEdge;
import gr.james.socialinfluence.graph.Vertex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Dot implements GraphImporter, GraphExporter {
    @Override
    public void to(Graph g, OutputStream out) throws IOException {
        if (g.isUndirected()) {
            ArrayList<Vertex[]> edgeList = new ArrayList<>();
            for (FullEdge e : g.getEdges()) {
                Vertex v = e.getSource();
                Vertex w = e.getTarget();
                int indexOfOpposite = -1;
                for (int i = 0; i < edgeList.size(); i++) {
                    if (edgeList.get(i)[0].equals(w) && edgeList.get(i)[1].equals(v)) {
                        indexOfOpposite = i;
                        break;
                    }
                }
                if (indexOfOpposite == -1) {
                    edgeList.add(new Vertex[]{v, w});
                }
            }

            String dot = "graph " + g.getMeta("name") + " {" + System.lineSeparator();
            dot += "  overlap = false;" + System.lineSeparator();
            dot += "  bgcolor = transparent;" + System.lineSeparator();
            dot += "  splines = true;" + System.lineSeparator();
            dot += "  dpi = 192;" + System.lineSeparator();
            dot += System.lineSeparator();
            dot += "  graph [fontname = \"Noto Sans\"];" + System.lineSeparator();
            dot += "  node [fontname = \"Noto Sans\", shape = circle, fixedsize = shape, penwidth = 2.0, color = \"#444444\", style = \"filled\", fillcolor = \"#CCCCCC\"];" + System.lineSeparator();
            dot += "  edge [fontname = \"Noto Sans\", penwidth = 2.0, color = \"#444444\"];" + System.lineSeparator();
            dot += System.lineSeparator();
            for (Vertex[] v : edgeList) {
                dot += "  " + v[0].toString() + " -- " + v[1].toString() + System.lineSeparator();
            }
            dot += "}" + System.lineSeparator();

            try {
                out.write(dot.getBytes(Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String dot = "digraph G {" + System.lineSeparator();
            dot += "  overlap = false;" + System.lineSeparator();
            dot += "  bgcolor = transparent;" + System.lineSeparator();
            dot += "  splines = true;" + System.lineSeparator();
            for (FullEdge e : g.getEdges()) {
                Vertex v = e.getSource();
                Vertex w = e.getTarget();
                dot += "  " + v.toString() + " -> " + w.toString() + System.lineSeparator();
            }
            dot += "}" + System.lineSeparator();

            try {
                out.write(dot.getBytes(Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Graph from(InputStream in) throws IOException {
        throw new UnsupportedOperationException();
    }
}