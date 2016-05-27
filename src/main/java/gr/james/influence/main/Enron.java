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
import java.util.stream.IntStream;

public class Enron {
    private static Graph g = new MemoryGraph();
    private static Map<VertexPair, Double> distanceMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Graph r = new Edges().from(new FileInputStream("C:\\Users\\James\\Downloads\\graph.csv"));

        String[] guilty = new String[]{"23", "32", "63", "118"};
        String[] potential = new String[]{"8", "43", "45", "55", "60", "62", "140"};

        // Convert all edge weights x to 2^(-x)
        //Graph g = new MemoryGraph();
        for (Vertex v : r) {
            g.addVertex(v);
        }
        for (Vertex v : r) {
            r.getOutEdges(v).entrySet().stream().filter(e -> r.containsVertex(e.getKey()))
                    .forEach(e -> g.addEdge(v, e.getKey(), 1.0e+11 * Math.pow(2.0, -e.getValue().getWeight())));
        }
        for (String m : r.metaKeySet()) {
            g.setMeta(m, r.getMeta(m));
        }

        // Stats
        System.out.println("EDGES: " + r.getEdgesCount());
        System.out.println("MEAN DEGREE: " + r.getVertices().stream().map(r::getOutDegree).mapToInt(i -> i).average().getAsDouble());
        System.out.println("MEAN STRENGTH: " + r.getVertices().stream().map(r::getOutStrength).mapToDouble(i -> i).average().getAsDouble());
        System.out.println("DIAMETER: " + r.getDiameter());

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
        distanceMap = Maps.newHashMap(FloydWarshall.execute(g));
        for (Vertex v : g) {
            distanceMap.put(new VertexPair(v, v), 0.0);
        }

        // Gamma (Experiment 1)
        System.out.println("# GAMMA (EXPERIMENT 1) #");
        List<Vertex> gammaList = produceResult(guiltyVertices);
        evaluate(gammaList, potentialVertices);

        // Permutations (Experiment 2)
        System.out.println("# PERMUTATIONS (EXPERIMENT 2) #");
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                System.out.printf("## PERMUTATION (%d,%d) ##%n", i, j);
                List<Vertex> inputSet = Arrays.asList(guiltyVertices.get(i), guiltyVertices.get(j));
                List<Vertex> evalList = new ArrayList<>(guiltyVertices);
                evalList.removeAll(inputSet);
                List<Vertex> permutationList = produceResult(inputSet);
                evaluate(permutationList, evalList);
            }
        }

        double h = 0;
    }

    private static List<Vertex> produceResult(List<Vertex> S) {
        Map<Vertex, Double> kCenter = new HashMap<>();
        for (Vertex v : g) {
            /*double d = S.stream().map(i -> distanceMap.get(new VertexPair(i, v)))
                    .mapToDouble(Math::log).sum();*/
            /*double d = 1 / S.stream().mapToDouble(i -> 1 / distanceMap.get(new VertexPair(i, v))).sum();*/
            double d = S.stream().mapToDouble(i -> 1 / distanceMap.get(new VertexPair(i, v))).sum();
            kCenter.put(v, d);
        }
        List<Vertex> sortedList = kCenter.entrySet().stream()
                .sorted((o1, o2) -> -Double.compare(o1.getValue(), o2.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        sortedList.removeAll(S);
        return sortedList;
    }

    private static void evaluate(List<Vertex> sortedList, List<Vertex> truth) {
        // indices
        List<Integer> indices = truth.stream().map(sortedList::indexOf).sorted().collect(Collectors.toList());
        System.out.printf("Indices: %s - Average Precision: %f%n", indices,
                IntStream.range(0, indices.size()).mapToDouble(i -> (i + 1.) / (indices.get(i) + 1.))
                        .average().getAsDouble());

        // recall
        System.out.printf("Recall: ");
        int soFar = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            if (truth.contains(sortedList.get(i))) {
                soFar++;
            }
            System.out.printf("(%d,%f) ", i + 1, (double) soFar / truth.size());
        }
        System.out.println();

        // precision
        System.out.printf("Precision: ");
        soFar = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            if (truth.contains(sortedList.get(i))) {
                soFar++;
            }
            System.out.printf("(%d,%f) ", i + 1, (double) soFar / (i + 1));
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
            System.out.printf("(%d,%f) ", i + 1, 2 * (prec * recall) / (prec + recall));
        }
        System.out.println();
    }
}
