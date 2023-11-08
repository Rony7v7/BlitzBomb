package Controller;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.interfaces.IGraph;
import model.BombWrapper;

import java.net.URL;
import java.util.ResourceBundle;

public class BlitzBombSystem implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        IGraph<String, BombWrapper> graph = generateRandomGraph();
        displayGraph(graph);
    }

    private IGraph<String, BombWrapper> generateRandomGraph() {
        IGraph<String, BombWrapper> graph = new GraphAL<>(GraphType.Simple); // You need to provide the appropriate
                                                                             // graph type
        LevelGenerator levelGenerator = new LevelGenerator(graph);
        return levelGenerator.generateRandomLevel(10, 4); // You can adjust the number of vertices and edges as needed
    }

    private void displayGraph(IGraph<String, BombWrapper> graph) {
        Pane root = new Pane();

        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            double centerX = Math.random() * 400;
            double centerY = Math.random() * 400;

            Circle circle = new Circle(centerX, centerY, 20);
            Text text = new Text(centerX - 5, centerY + 5, vertex.getKey());
            root.getChildren().addAll(circle, text);
        }

        for (Edge<String, BombWrapper> edge : graph.getEdgeList()) {
            Vertex<String, BombWrapper> vertex1 = edge.getVertex1();
            Vertex<String, BombWrapper> vertex2 = edge.getVertex2();
            double x1 = Math.random() * 400; // Random x position for vertex 1
            double y1 = Math.random() * 400; // Random y position for vertex 1
            double x2 = Math.random() * 400; // Random x position for vertex 2
            double y2 = Math.random() * 400;

            Line line = new Line(x1, y1, x2, y2);
            root.getChildren().add(line);
        }

        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Graph Visualization");
        stage.setScene(scene);
        stage.show();
    }
}
