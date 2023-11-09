package Controller;

import structures.classes.Edge;
import structures.classes.Vertex;
import structures.interfaces.IGraph;
import model.Bomb;
import model.BombWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LevelGenerator {
    private IGraph<String, BombWrapper> graph;

    private ArrayList<Vertex<String, BombWrapper>> row1;
    private ArrayList<Vertex<String, BombWrapper>> row2;
    private ArrayList<Vertex<String, BombWrapper>> row3;
    private ArrayList<Vertex<String, BombWrapper>> row4;
    private ArrayList<Vertex<String, BombWrapper>> row5;

    private ArrayList<Vertex<String, BombWrapper>> vertices = new ArrayList<>();

    public LevelGenerator(IGraph<String, BombWrapper> graph) {
        this.graph = graph;
        this.row1 = new ArrayList<>();
        this.row2 = new ArrayList<>();
        this.row3 = new ArrayList<>();
        this.row4 = new ArrayList<>();
        this.row5 = new ArrayList<>();
    }

    public IGraph<String, BombWrapper> generateRandomLevel(int numVertices, int maxEdgesPerVertex, double canvasHeight,
            double canvasWidth) {
        Random random = new Random();
        double radius = 15.0;

        // Agregar vértices al grafo
        for (int i = 0; i < numVertices; i++) {
            double x = random.nextDouble() * (canvasWidth - 2 * radius) + radius; // Random X position within canvas
            double y = random.nextDouble() * (canvasHeight - 2 * radius) + radius; // Random Y position within canvas
            // Adjust vertex position to prevent intersections

            for (Vertex<String, BombWrapper> existingVertex : vertices) {
                double distance = Math.sqrt(Math.pow(x - existingVertex.getValue().X, 2) +
                        Math.pow(y - existingVertex.getValue().Y, 2));

                if (distance < radius * 2) {
                    // Random X position within canvas
                    y = random.nextDouble() * (canvasHeight - 2 * radius) + radius; // Random Y position within canvas
                }
            }

            vertices.add(graph.insertVertex("Vertex " + i, new BombWrapper(x, y, new Bomb(), radius)));
        }

        Collections.shuffle(vertices);

        while (vertices.size() >= maxEdgesPerVertex) {
            Vertex<String, BombWrapper> vertex = vertices.get(0);

            while (vertex.getEdges().size() < maxEdgesPerVertex) {
                int randomIndex = random.nextInt(vertices.size());
                int randomWeight = random.nextInt(10) + 1;
                if (!vertex.isConnected(vertices.get(randomIndex)) && vertex != vertices.get(randomIndex)) {
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
        return graph;
    }

    private ArrayList<Vertex<String, BombWrapper>> createVertexRow(double positionY, int amountVertex, int radius,
            int canvasWidth) {
        ArrayList<Vertex<String, BombWrapper>> row = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < amountVertex; i++) {
            row.add(graph.insertVertex("Vertex " + i, new BombWrapper(
                    random.nextDouble() * (canvasWidth - 2 * radius) + radius, positionY, new Bomb(), radius)));
        }
        return row;
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
