package Controller;

import structures.classes.Edge;
import structures.classes.Vertex;
import structures.interfaces.IGraph;
import model.Bomb;
import model.BombWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelGenerator {
    private IGraph<String, BombWrapper> graph;

    private ArrayList<Vertex<String, BombWrapper>> row1;
    private ArrayList<Vertex<String, BombWrapper>> row2;
    private ArrayList<Vertex<String, BombWrapper>> row3;
    private ArrayList<Vertex<String, BombWrapper>> row4;
    private ArrayList<Vertex<String, BombWrapper>> row5;
    private ArrayList<List<Vertex<String, BombWrapper>>> rows;

    private static final double VERTEX_RADIUS = 20.0;
    private static final double BOMBS_PERCENTAGE = 0.5;

    public LevelGenerator(IGraph<String, BombWrapper> graph) {
        this.graph = graph;
        this.row1 = new ArrayList<>();
        this.row2 = new ArrayList<>();
        this.row3 = new ArrayList<>();
        this.row4 = new ArrayList<>();
        this.row5 = new ArrayList<>();
        this.rows = new ArrayList<>();

    }

    public IGraph<String, BombWrapper> generateRandomLevel(int numVertices, int maxEdgesPerVertex, double canvasHeight,
            double canvasWidth) {
        int amountOfRows = 5;
        row1 = createVertexRow(canvasHeight / 2 + 300, numVertices / amountOfRows, VERTEX_RADIUS, canvasWidth);
        row2 = createVertexRow(canvasHeight / 2 + 150, numVertices / amountOfRows, VERTEX_RADIUS, canvasWidth);
        row3 = createVertexRow(canvasHeight / 2, numVertices / amountOfRows, VERTEX_RADIUS, canvasWidth);
        row4 = createVertexRow(canvasHeight / 2 - 150, numVertices / amountOfRows, VERTEX_RADIUS, canvasWidth);
        row5 = createVertexRow(canvasHeight / 2 - 300, numVertices / amountOfRows, VERTEX_RADIUS, canvasWidth);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
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

        linkVertices(row1, row2);
        linkVertices(row2, row3);
        linkVertices(row3, row4);
        linkVertices(row4, row5);

        for (int i = 0; i < 5; i++) {
            connectRow(rows.get(i));
        }

        for (int i = 0; i < 5; i++) {
            generateRandomBombs(rows.get(i), BOMBS_PERCENTAGE);
        }

        return graph;
    }

    private void generateRandomBombs(List<Vertex<String, BombWrapper>> row, double percentage) {
        Random random = new Random();

        for (int i = 0; i < (int) (row.size() * percentage); i++) {
            boolean hasBomb = false;
            do {
                int randomIndex = random.nextInt(row.size());
                if (row.get(randomIndex).getValue().getBomb() == null) {
                    row.get(randomIndex).getValue().setBomb(new Bomb());
                    hasBomb = true;
                }
            } while (!hasBomb);
        }

    }

    private ArrayList<Vertex<String, BombWrapper>> createVertexRow(double positionY, int amountVertex, double radius,
            double canvasWidth) {
        ArrayList<Vertex<String, BombWrapper>> row = new ArrayList<>();
        double positionX = 0;
        double spaceBweteenVertex = (canvasWidth - 2 * radius) / amountVertex;
        for (int i = 0; i < amountVertex; i++) {

            positionX = spaceBweteenVertex * i + radius;

            row.add(new Vertex<String, BombWrapper>("Vertex " + i,
                    new BombWrapper(positionX, positionY, null, radius)));
        }
        return row;
    }

    private void connectRow(List<Vertex<String, BombWrapper>> row) {
        for (int i = 0; i < row.size(); i++) {
            if (i == 0) {
                Edge<String, BombWrapper> edge = new Edge<>(row.get(i), row.get(i + 1), 1);
                graph.insertEdge(edge);
            } else if (i == row.size() - 1) {
                Edge<String, BombWrapper> edge = new Edge<>(row.get(i), row.get(i - 1), 1);
                graph.insertEdge(edge);
            } else {
                Edge<String, BombWrapper> edge1 = new Edge<>(row.get(i), row.get(i - 1), 1);
                Edge<String, BombWrapper> edge2 = new Edge<>(row.get(i), row.get(i + 1), 1);
                graph.insertEdge(edge1);
                graph.insertEdge(edge2);
            }
        }
    }

    private void linkVertices(ArrayList<Vertex<String, BombWrapper>> row1,
            ArrayList<Vertex<String, BombWrapper>> row2) {

        Random random = new Random();

        for (int i = 0; i < row1.size(); i++) {
            boolean isLinked = false;
            Vertex<String, BombWrapper> vertex = row1.get(i);
            do {

                int randomIndex = random.nextInt(row2.size());
                int randomWeight = random.nextInt(10) + 1;
                if (row2.get(randomIndex).getEdges().size() < 1) {
                    Edge<String, BombWrapper> edge = new Edge<>(vertex,
                            row2.get(randomIndex), randomWeight);
                    graph.insertEdge(edge);
                    isLinked = true;
                }
            } while (!isLinked);

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
