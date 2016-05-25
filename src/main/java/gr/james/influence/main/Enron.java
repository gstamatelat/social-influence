package gr.james.influence.main;

import com.google.common.collect.Maps;
import gr.james.influence.algorithms.distance.FloydWarshall;
import gr.james.influence.algorithms.scoring.Degree;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.MemoryGraph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.graph.io.Edges;
import gr.james.influence.util.collections.GraphState;
import gr.james.influence.util.collections.VertexPair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Enron {
    public static void main(String[] args) throws IOException {
        Graph g = new Edges().from(new FileInputStream("C:\\Users\\James\\Downloads\\graph.csv"));

        String[] guilty = new String[]{"23", "32", "63", "118"};
        String[] potential = new String[]{"8", "43", "45", "55", "60", "62", "140"};

        // Convert all edge weights x to 2^(-x)
        Graph r = new MemoryGraph();
        for (Vertex v : g) {
            r.addVertex(v);
        }
        for (Vertex v : r) {
            g.getOutEdges(v).entrySet().stream().filter(e -> r.containsVertex(e.getKey()))
                    .forEach(e -> r.addEdge(v, e.getKey(), 1.0e+11 * Math.pow(2.0, -e.getValue().getWeight())));
        }
        for (String m : g.metaKeySet()) {
            r.setMeta(m, g.getMeta(m));
        }
        g = r;

        // In Degree distribution
        GraphState<Integer> inDeg = Degree.execute(g, true);
        System.out.printf("IN DEGREE: ");
        int maxIn = inDeg.values().stream().mapToInt(i -> i).max().getAsInt();
        int[] distIn = new int[maxIn + 1];
        for (int d : inDeg.values()) {
            distIn[d]++;
        }
        for (int i = 0; i < distIn.length; i++) {
            System.out.printf("(%d %d) ", i, distIn[i]);
        }
        System.out.println();

        // Out Degree distribution
        GraphState<Integer> outDeg = Degree.execute(g, false);
        System.out.printf("OUT DEGREE: ");
        int maxOut = outDeg.values().stream().mapToInt(i -> i).max().getAsInt();
        int[] distOut = new int[maxOut + 1];
        for (int d : outDeg.values()) {
            distOut[d]++;
        }
        for (int i = 0; i < distOut.length; i++) {
            System.out.printf("(%d %d) ", i, distOut[i]);
        }
        System.out.println();

        // Get guilty vertices
        List<Vertex> guiltyVertices = new ArrayList<>();
        for (Vertex v : g) {
            if (Arrays.asList(guilty).contains(v.getLabel())) {
                guiltyVertices.add(v);
            }
        }

        // Get potential vertices
        List<Vertex> potentialVertices = new ArrayList<>();
        for (Vertex v : g) {
            if (Arrays.asList(potential).contains(v.getLabel())) {
                potentialVertices.add(v);
            }
        }

        // Distance matrix
        Map<VertexPair, Double> distanceMap = Maps.newHashMap(FloydWarshall.execute(g));
        for (Vertex v : g) {
            distanceMap.put(new VertexPair(v, v), 0.0);
        }

        // k-center
        Map<Vertex, Double> kCenter = new HashMap<>();
        for (Vertex v : g) {
            double d = guiltyVertices.stream().map(i -> distanceMap.get(new VertexPair(i, v)))
                    .mapToDouble(Math::log).sum();
            kCenter.put(v, d);
        }
        List<Vertex> sortedList = kCenter.entrySet().stream()
                .sorted((o1, o2) -> Double.compare(o1.getValue(), o2.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        sortedList.removeAll(guiltyVertices);

        System.out.println("# K-CENTER #");
        evaluate(sortedList, potentialVertices);

        double h = 0;
    }

    private static void evaluate(List<Vertex> sortedList, List<Vertex> truth) {
        // indices
        System.out.printf("INDICES: ");
        System.out.println(truth.stream().map(sortedList::indexOf).sorted().collect(Collectors.toList()));

        // recall
        System.out.printf("RECALL: ");
        int soFar = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            if (truth.contains(sortedList.get(i))) {
                soFar++;
            }
            System.out.printf("(%d %f) ", i + 1, (double) soFar / truth.size());
        }
        System.out.println();

        // precision
        System.out.printf("PRECISION: ");
        soFar = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            if (truth.contains(sortedList.get(i))) {
                soFar++;
            }
            System.out.printf("(%d %f) ", i + 1, (double) soFar / (i + 1));
        }
        System.out.println();

        // f1
        System.out.printf("F1: ");
        soFar = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            if (truth.contains(sortedList.get(i))) {
                soFar++;
            }
            double prec = (double) soFar / (i + 1);
            double recall = (double) soFar / truth.size();
            System.out.printf("(%d %f) ", i + 1, 2 * (prec * recall) / (prec + recall));
        }
        System.out.println();
    }
}
