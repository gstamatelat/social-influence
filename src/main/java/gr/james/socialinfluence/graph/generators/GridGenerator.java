package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphGenerator;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.helper.Helper;

import java.util.Set;

public class GridGenerator<T extends Graph> implements GraphGenerator<T> {
    private Class<T> type;
    private int rows,columns;

    public GridGenerator(Class<T> type, int rows, int columns) {
        this.type = type;
        this.rows = rows;
        this.columns = columns;
    }
    

    @Override
    public T create() {
        T g = Helper.instantiateGeneric(type);

        int count = 0;
        Set<Vertex> set = g.addVertices(rows * columns);
        Vertex[] a = set.toArray(new Vertex[rows * columns]);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (j != columns - 1) {
                    g.addEdge(a[count], a[count + 1], true);
                }
                if (i != rows - 1) {
                    g.addEdge(a[count], a[count + columns], true);
                }

                count = count + 1;
            }
        }
        g.setMeta("type", "Grid")
                .setMeta("rows", String.valueOf(rows))
                .setMeta("columns", String.valueOf(columns));

        return g;
    }
}
