package test;

import org.junit.Test;

import structures.classes.GraphAL;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.classes.Edge;

import static org.junit.Assert.*;

import java.util.List;

public class DijkstraTest {

    // Returns the shortest path between two vertices in a connected graph
    @Test
    public void test_shortest_path_connected_graph() {
        GraphAL<String, Integer> graph = new GraphAL<>(GraphType.Simple);
        Vertex<String, Integer> vertexA = graph.insertVertex("A", 0);
        Vertex<String, Integer> vertexB = graph.insertVertex("B", 1);
        Vertex<String, Integer> vertexC = graph.insertVertex("C", 2);
        Vertex<String, Integer> vertexD = graph.insertVertex("D", 3);
        Vertex<String, Integer> vertexE = graph.insertVertex("E", 4);

        graph.insertEdge(new Edge<>(vertexA, vertexB, 1));
        graph.insertEdge(new Edge<>(vertexA, vertexC, 4));
        graph.insertEdge(new Edge<>(vertexB, vertexC, 2));
        graph.insertEdge(new Edge<>(vertexB, vertexD, 5));
        graph.insertEdge(new Edge<>(vertexC, vertexD, 1));
        graph.insertEdge(new Edge<>(vertexC, vertexE, 3));
        graph.insertEdge(new Edge<>(vertexD, vertexE, 2));

        List<Edge<String, Integer>> shortestPath = graph.Dijkstra(vertexA, vertexE);
        assertEquals(3, shortestPath.size());
        assertEquals(vertexB, shortestPath.get(0).getVertex2());
        assertEquals(vertexC, shortestPath.get(1).getVertex2());
        assertEquals(vertexE, shortestPath.get(2).getVertex2());
    }

    // Returns an empty list if the source and destination vertices are the same
    @Test
    public void test_empty_list_same_source_and_destination() {
        GraphAL<String, Integer> graph = new GraphAL<>(GraphType.Simple);
        Vertex<String, Integer> vertexA = graph.insertVertex("A", 0);

        List<Edge<String, Integer>> shortestPath = graph.Dijkstra(vertexA, vertexA);

        assertTrue(shortestPath.isEmpty());
    }

    // Returns a list with a single edge if the source and destination vertices are
    // adjacent
    @Test
    public void test_single_edge_adjacent_vertices() {
        GraphAL<String, Integer> graph = new GraphAL<>(GraphType.Simple);
        Vertex<String, Integer> vertexA = graph.insertVertex("A", 0);
        Vertex<String, Integer> vertexB = graph.insertVertex("B", 1);

        graph.insertEdge(new Edge<>(vertexA, vertexB, 1));

        List<Edge<String, Integer>> shortestPath = graph.Dijkstra(vertexA, vertexB);

        assertEquals(vertexB, shortestPath.get(0).getVertex2());
    }

    // Returns an empty list if the source or destination vertex is not in the graph
    @Test
    public void test_empty_list_vertex_not_in_graph() {
        GraphAL<String, Integer> graph = new GraphAL<>(GraphType.Simple);
        Vertex<String, Integer> vertexA = graph.insertVertex("A", 0);
        Vertex<String, Integer> vertexB = new Vertex<>("B", 1);

        List<Edge<String, Integer>> shortestPath = graph.Dijkstra(vertexA, vertexB);

        assertTrue(shortestPath.isEmpty());
    }

    // Returns an empty list if there is no path between the source and destination
    // vertices
    @Test
    public void test_empty_list_no_path_between_vertices() {
        GraphAL<String, Integer> graph = new GraphAL<>(GraphType.Simple);
        Vertex<String, Integer> vertexA = graph.insertVertex("A", 0);
        Vertex<String, Integer> vertexB = graph.insertVertex("B", 1);
        Vertex<String, Integer> vertexC = graph.insertVertex("C", 2);

        graph.insertEdge(new Edge<>(vertexA, vertexB, 1));

        List<Edge<String, Integer>> shortestPath = graph.Dijkstra(vertexA, vertexC);

        assertTrue(shortestPath.isEmpty());
    }
}