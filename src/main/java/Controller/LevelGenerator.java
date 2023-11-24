package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Bomb;
import model.BombWrapper;
import model.enums.TypeOfNode;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.GraphAM;
import structures.classes.Vertex;
import structures.interfaces.IGraph;

public class LevelGenerator {
    private final IGraph<String, BombWrapper> graph;
    private final List<List<Vertex<String, BombWrapper>>> rows;
    private static final double VERTEX_RADIUS = 20.0;
    private static final double BOMBS_PERCENTAGE = 0.5;
    private int amountOfBombs;

    public LevelGenerator(IGraph<String, BombWrapper> graph) {
        this.graph = graph;
        this.rows = new ArrayList<>();
    }

    /**
     * Generates a random level graph with the specified number of vertices, canvas height, and canvas width.
     * 
     * @param numVertices   The number of vertices in the graph.
     * @param canvasHeight  The height of the canvas.
     * @param canvasWidth   The width of the canvas.
     * @return              The generated graph.
     */
    public IGraph<String, BombWrapper> generateRandomLevel(int numVertices, double canvasHeight,
            double canvasWidth) {
        int amountOfRows = 5;
        List<Vertex<String, BombWrapper>> row1 = createVertexRow(canvasHeight / 2 + 300, numVertices / amountOfRows,
                VERTEX_RADIUS, canvasWidth);
        List<Vertex<String, BombWrapper>> row2 = createVertexRow(canvasHeight / 2 + 150, numVertices / amountOfRows,
                VERTEX_RADIUS, canvasWidth);
        List<Vertex<String, BombWrapper>> row3 = createVertexRow(canvasHeight / 2, numVertices / amountOfRows,
                VERTEX_RADIUS, canvasWidth);
        List<Vertex<String, BombWrapper>> row4 = createVertexRow(canvasHeight / 2 - 150, numVertices / amountOfRows,
                VERTEX_RADIUS, canvasWidth);
        List<Vertex<String, BombWrapper>> row5 = createVertexRow(canvasHeight / 2 - 300, numVertices / amountOfRows,
                VERTEX_RADIUS, canvasWidth);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        for (int i = 0; i < numVertices - 1; i++) {
            Vertex<String, BombWrapper> vertex;
            if (i < numVertices / amountOfRows) {
                vertex = row1.get(i);
            } else if (i < 2 * numVertices / amountOfRows) {
                vertex = row2.get(i - numVertices / amountOfRows);
            } else if (i < 3 * numVertices / amountOfRows) {
                vertex = row3.get(i - 2 * numVertices / amountOfRows);
            } else if (i < 4 * numVertices / amountOfRows) {
                vertex = row4.get(i - 3 * numVertices / amountOfRows);
            } else {
                vertex = row5.get(i - 4 * numVertices / amountOfRows);
            }
            vertex.setKey("Vertex " + i);
            graph.insertVertex(vertex);
        }

        createSpawnAndEnd();

        if (graph instanceof GraphAL) {
            linkVerticesAL(row1, row2);
            linkVerticesAL(row2, row3);
            linkVerticesAL(row3, row4);
            linkVerticesAL(row4, row5);
        } else {
            linkVerticesAM(row1, row2);
            linkVerticesAM(row2, row3);
            linkVerticesAM(row3, row4);
            linkVerticesAM(row4, row5);
        }

        for (List<Vertex<String, BombWrapper>> row : rows) {
            connectRow(row);
            generateRandomBombs(row, BOMBS_PERCENTAGE);
        }

        return graph;
    }

    public int amountOfBombs() {
        return this.amountOfBombs;
    }

    /**
     * Sets the type of the first vertex in the graph to SPAWN and the type of the last vertex to END.
     */
    private void createSpawnAndEnd() {
        graph.getVertexList().get(0).getValue().setType(TypeOfNode.SPAWN);
        graph.getVertexList().get(graph.getVertexList().size() - 1).getValue().setType(TypeOfNode.END);
    }

    /**
     * Generates random bombs in a given row based on a specified percentage.
     * 
     * @param row        the list of vertices representing the row
     * @param percentage the percentage of bombs to generate
     */
    public void generateRandomBombs(List<Vertex<String, BombWrapper>> row, double percentage) {
        Random random = new Random();
        int numBombs = (int) (row.size() * percentage);
        int attempts = 0;
        for (int i = 0; i < numBombs; i++) {
            boolean hasBomb = false;
            do {
                int randomIndex = random.nextInt(row.size());
                if (row.get(randomIndex).getValue().getType().equals(TypeOfNode.SPAWN)
                        || row.get(randomIndex).getValue().getType().equals(TypeOfNode.END)) {
                    break;
                }
                if (row.get(randomIndex).getValue().getBomb() == null) {
                    row.get(randomIndex).getValue().setBomb(new Bomb());
                    this.amountOfBombs++;
                    hasBomb = true;
                }
                attempts++;
                if (attempts >= 100) {
                    break;
                }
            } while (!hasBomb);
        }
    }

    /**
     * Creates a row of vertices with the specified parameters.
     *
     * @param positionY     the y-coordinate of the row
     * @param amountVertex  the number of vertices in the row
     * @param radius        the radius of each vertex
     * @param canvasWidth   the width of the canvas
     * @return              a list of vertices representing the row
     */
    public List<Vertex<String, BombWrapper>> createVertexRow(double positionY, int amountVertex, double radius,
            double canvasWidth) {
        List<Vertex<String, BombWrapper>> row = new ArrayList<>();
        double positionX;
        double spaceBweteenVertex = (canvasWidth - 2 * radius) / amountVertex;
        for (int i = 0; i < amountVertex; i++) {
            positionX = spaceBweteenVertex * i + radius;
            row.add(new Vertex<>("Vertex " + i, new BombWrapper(positionX, positionY, null, radius)));
        }
        return row;
    }

    /**
     * Connects the vertices in a row by creating edges between them in the graph.
     * The edges are created based on the position of the vertices in the row.
     * If a vertex is the first or last in the row, it is connected to its adjacent vertex.
     * If a vertex is in the middle of the row, it is connected to both its adjacent vertices.
     *
     * @param row the list of vertices representing a row in the graph
     */
    public void connectRow(List<Vertex<String, BombWrapper>> row) {
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

    /**
     * Links the vertices in row1 to the vertices in row2 by creating random edges with random weights.
     * Only vertices in row2 that have less than 1 edge are considered for linking.
     *
     * @param row1 The list of vertices in row1.
     * @param row2 The list of vertices in row2.
     */
    private void linkVerticesAL(List<Vertex<String, BombWrapper>> row1, List<Vertex<String, BombWrapper>> row2) {
        Random random = new Random();
        for (Vertex<String, BombWrapper> vertex : row1) {
            boolean isLinked = false;
            do {
                int randomIndex = random.nextInt(row2.size());
                int randomWeight = random.nextInt(10) + 1;
                if (row2.get(randomIndex).getEdges().size() < 1) {
                    Edge<String, BombWrapper> edge = new Edge<>(vertex, row2.get(randomIndex), randomWeight);
                    graph.insertEdge(edge);
                    isLinked = true;
                }
            } while (!isLinked);
        }
    }

    /**
     * Links the vertices in row1 to the vertices in row2 by creating random weighted edges between them.
     * Only vertices in row2 that have less than 1 connection are considered for linking.
     *
     * @param row1 The list of vertices in row1.
     * @param row2 The list of vertices in row2.
     */
    private void linkVerticesAM(List<Vertex<String, BombWrapper>> row1, List<Vertex<String, BombWrapper>> row2) {
        Random random = new Random();
        for (Vertex<String, BombWrapper> vertex : row1) {
            boolean isLinked = false;
            do {
                int randomIndex = random.nextInt(row2.size());
                int randomWeight = random.nextInt(10) + 1;
                if (calculateAmountOfConnections(row2.get(randomIndex)) < 1) {
                    Edge<String, BombWrapper> edge = new Edge<>(vertex, row2.get(randomIndex), randomWeight);
                    graph.insertEdge(edge);
                    isLinked = true;
                }
            } while (!isLinked);
        }
    }

    /**
     * Calculates the amount of connections for a given vertex in the graph.
     * 
     * @param vertex The vertex for which to calculate the amount of connections.
     * @return The number of connections for the given vertex.
     */
    private int calculateAmountOfConnections(Vertex<String, BombWrapper> vertex) {
        int vertexIndex = graph.getVertexList().indexOf(vertex);
        int amountOfConnections = 0;
        GraphAM<String, BombWrapper> graphAM = (GraphAM<String, BombWrapper>) graph;
        ArrayList<Edge<String, BombWrapper>> edges = graphAM.getAdjacencyMatrix().get(vertexIndex);
        for (Edge<String, BombWrapper> edge : edges) {
            if (edge != null) {
                amountOfConnections++;
            }
        }
        return amountOfConnections;
    }

}
