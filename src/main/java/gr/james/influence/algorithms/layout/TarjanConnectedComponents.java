package gr.james.influence.algorithms.layout;

import com.google.common.collect.BiMap;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <p>Implementation of the <a href="https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm">
 * Tarjan's strongly connected components algorithm</a>. This algorithm is implemented recursively and is prone to
 * StackOverflowException on large graphs.</p>
 *
 * @see KosarajuConnectedComponents
 */
public class TarjanConnectedComponents<V> {
    private Graph<V, ?> g;
    private BiMap<V, Integer> vertexMap;

    private boolean[] visited;
    private Stack<Integer> stack;
    private int time;
    private int[] lowlink;
    private List<List<V>> components;

    public List<List<V>> execute(Graph<V, ?> g) {
        vertexMap = Graphs.getGraphIndexMap(g);

        this.g = g;
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

        assert Helper.listToSet(components).equals(KosarajuConnectedComponents.execute(g));
        assert components.stream().mapToInt(List::size).sum() == g.getVerticesCount();

        return components;
    }

    private void dfs(int u) {
        lowlink[u] = time++;
        visited[u] = true;
        stack.add(u);
        boolean isComponentRoot = true;

        for (V v0 : g.getOutEdges(vertexMap.inverse().get(u)).keySet()) {
            int w = vertexMap.get(v0);
            if (!visited[w]) {
                dfs(w);
            }
            if (lowlink[u] > lowlink[w]) {
                lowlink[u] = lowlink[w];
                isComponentRoot = false;
            }
        }

        if (isComponentRoot) {
            List<V> component = new ArrayList<>();
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
