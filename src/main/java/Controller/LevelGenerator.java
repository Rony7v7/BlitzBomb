package Controller;

import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.interfaces.IGraph;
import model.BombWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LevelGenerator {
    IGraph<String, BombWrapper> graph;

    public LevelGenerator(GraphType type) {
        this.graph = new GraphAL<>(type);
        ;
    }

    public IGraph<String, BombWrapper> generateRandomLevel(int numVertices, int maxEdgesPerVertex) {
        Random random = new Random();

        ArrayList<Vertex<String, BombWrapper>> vertices = new ArrayList<>();

        // Agregar vértices al grafo
        for (int i = 0; i < numVertices; i++) {
            vertices.add(graph.insertVertex("Vertex " + i, new BombWrapper()));
        }

        Collections.shuffle(vertices);
        while (!vertices.isEmpty()) {
            Vertex<String, BombWrapper> vertex = vertices.get(0);
            while (vertex.getEdges().size() < maxEdgesPerVertex) {
                int randomIndex = random.nextInt(vertices.size());
                int randomWeight = random.nextInt(10) + 1;
                if (!vertex.isConnected(vertices.get(randomIndex))) {
                    Edge<String, BombWrapper> edge = new Edge<>(vertex, vertices.get(randomIndex), randomWeight);
                    graph.insertEdge(edge);
                }
            }

            for (int j = 0; j < vertices.size(); j++) {
                if (vertices.get(j).getEdges().size() >= maxEdgesPerVertex) {
                    vertices.remove(j);
                }
            }
        }
        cleanEdges();
        return graph;
    }

    private boolean validateMinVertex(ArrayList<Vertex<?, ?>> vertices, Vertex<?, ?> vertex, int maxEdgesPerVertex) {
        if (vertices.size() >= maxEdgesPerVertex) {
            return vertex.getEdges().size() < maxEdgesPerVertex;
        } else {
            return vertex.getEdges().size() < vertices.size();
        }
    }

    public static void main(String args[]) {

        LevelGenerator levelGenerator = new LevelGenerator(GraphType.Simple);

        IGraph<String, BombWrapper> randomLevel = levelGenerator.generateRandomLevel(50, 4);

        levelGenerator.printGraphInConsole(randomLevel);
    }

    private void cleanEdges() {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            Set<Edge<String, BombWrapper>> uniqueEdges = new HashSet<>();
            List<Edge<String, BombWrapper>> edgesToRemove = new ArrayList<>();

            for (Edge<String, BombWrapper> edge : vertex.getEdges()) {
                // Si el conjunto no contiene la arista, la agregamos como única
                if (!uniqueEdges.add(edge)) {
                    edgesToRemove.add(edge);
                }
            }

            // Eliminamos las aristas duplicadas del vértice
            for (Edge<String, BombWrapper> edgeToRemove : edgesToRemove) {
                vertex.removeEdge(edgeToRemove.getVertex2());
            }
        }
    }

    public void printGraphInConsole(IGraph<String, BombWrapper> graph) {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            System.out.print("Vértice " + vertex.getKey() + ": ");
            for (Edge<String, BombWrapper> edge : vertex.getEdges()) {
                Vertex<String, BombWrapper> targetVertex = edge.getVertex2();
                System.out.print(targetVertex.getKey() + " ");
            }
            System.out.println();
        }
    }

}
