package Controller;

import structures.classes.Edge;
import structures.classes.Vertex;
import structures.interfaces.IGraph;
import model.BombWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LevelGenerator {
    IGraph<String, BombWrapper> graph;

    public LevelGenerator(IGraph<String, BombWrapper> graph) {
        this.graph = graph;
    }

    public IGraph<String, BombWrapper> generateRandomLevel(int numVertices, int maxEdgesPerVertex) {
        Random random = new Random();

        ArrayList<Vertex<String, BombWrapper>> vertices = new ArrayList<>();

        // Agregar vértices al grafo
        for (int i = 0; i < numVertices; i++) {
            vertices.add(graph.insertVertex("Vertex " + i, new BombWrapper()));
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
