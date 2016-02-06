package gr.james.influence.algorithms.layout;

import com.google.common.collect.BiMap;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.GraphUtils;
import gr.james.influence.graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <p>Implementation of the <a href="https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm">
 * Tarjan's strongly connected components algorithm</a>.</p>
 */
public class Tarjan {
    private static Graph g;
    private static BiMap<Vertex, Integer> vertexMap;

    private static boolean[] visited;
    private static Stack<Integer> stack;
    private static int time;
    private static int[] lowlink;
    private static List<List<Vertex>> components;

    public static List<List<Vertex>> execute(Graph g) {
        vertexMap = GraphUtils.getGraphIndexMap(g);

        Tarjan.g = g;
        visited = new boolean[g.getVerticesCount()];
        stack = new Stack<>();
        time = 0;
        lowlink = new int[g.getVerticesCount()];
        components = new ArrayList<>();

        for (int u = 0; u < g.getVerticesCount(); u++) {
            if (!visited[u]) {
                dfs(u);
            }
        }

        return components;
    }

    private static void dfs(int u) {
        lowlink[u] = time++;
        visited[u] = true;
        stack.add(u);
        boolean isComponentRoot = true;

        for (Vertex v0 : g.getOutEdges(vertexMap.inverse().get(u)).keySet()) {
            int v = vertexMap.get(v0);
            if (!visited[v]) {
                dfs(v);
            }
            if (lowlink[u] > lowlink[v]) {
                lowlink[u] = lowlink[v];
                isComponentRoot = false;
            }
        }

        if (isComponentRoot) {
            List<Vertex> component = new ArrayList<>();
            while (true) {
                int x = stack.pop();
                component.add(vertexMap.inverse().get(x));
                lowlink[x] = Integer.MAX_VALUE;
                if (x == u) {
                    break;
                }
            }
            components.add(component);
        }
    }
}
