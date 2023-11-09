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

    private static int amountOfVertecesInGrapg = 0;

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
        double radius = 15.0;
        int amountOfRows = 5;
        row1 = createVertexRow(canvasHeight / 6, numVertices / amountOfRows, radius, canvasWidth);
        row2 = createVertexRow(canvasHeight / 5, numVertices / amountOfRows, radius, canvasWidth);
        row3 = createVertexRow(canvasHeight / 4, numVertices / amountOfRows, radius, canvasWidth);
        row4 = createVertexRow(canvasHeight / 3, numVertices / amountOfRows, radius, canvasWidth);
        row5 = createVertexRow(canvasHeight / 2, numVertices / amountOfRows, radius, canvasWidth);
        for (int i = 0; i < numVertices - 1; i++) {
            if (i < numVertices / amountOfRows) {
                Vertex<String, BombWrapper> vertex = row1.get(i);
                vertex.setKey("Vertex " + i);
                graph.insertVertex(vertex);
            } else if (i < 2 * numVertices / amountOfRows) {
                Vertex<String, BombWrapper> vertex = row2.get(i - numVertices / amountOfRows);
                vertex.setKey("Vertex " + i);
                graph.insertVertex(vertex);
            } else if (i < 3 * numVertices / amountOfRows) {
                Vertex<String, BombWrapper> vertex = row3.get(i - 2 * numVertices / amountOfRows);
                vertex.setKey("Vertex " + i);
                graph.insertVertex(vertex);
            } else if (i < 4 * numVertices / amountOfRows) {
                Vertex<String, BombWrapper> vertex = row4.get(i - 3 * numVertices / amountOfRows);
                vertex.setKey("Vertex " + i);
                graph.insertVertex(vertex);
            } else if (i < 5 * numVertices / amountOfRows) {
                Vertex<String, BombWrapper> vertex = row5.get(i - 4 * numVertices / amountOfRows);
                vertex.setKey("Vertex " + i);
                graph.insertVertex(vertex);
            }

        }

        // Collections.shuffle(vertices);

        // while (vertices.size() >= maxEdgesPerVertex) {
        // Vertex<String, BombWrapper> vertex = vertices.get(0);

        // while (vertex.getEdges().size() < maxEdgesPerVertex) {
        // int randomIndex = random.nextInt(vertices.size());
        // int randomWeight = random.nextInt(10) + 1;
        // if (!vertex.isConnected(vertices.get(randomIndex)) && vertex !=
        // vertices.get(randomIndex)) {
        // Edge<String, BombWrapper> edge = new Edge<>(vertex,
        // vertices.get(randomIndex), randomWeight);
        // graph.insertEdge(edge);
        // }
        // }

        // for (int j = 0; j < vertices.size(); j++) {
        // if (vertices.get(j).getEdges().size() >= maxEdgesPerVertex) {
        // vertices.remove(j);
        // }
        // }
        // }
        return graph;
    }

    private ArrayList<Vertex<String, BombWrapper>> createVertexRow(double positionY, int amountVertex, double radius,
            double canvasWidth) {
        ArrayList<Vertex<String, BombWrapper>> row = new ArrayList<>();
        double positionX = 0;
        double spaceBweteenVertex = (canvasWidth - 2 * radius) / amountVertex;
        for (int i = 0; i < amountVertex; i++) {

            positionX = spaceBweteenVertex * i + radius;

            row.add(new Vertex<String, BombWrapper>("Vertex " + i,
                    new BombWrapper(positionX, positionY, new Bomb(), radius)));
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
