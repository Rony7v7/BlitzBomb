package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import structures.classes.GraphAL;
import structures.classes.Vertex;
import structures.enums.Color;
import structures.interfaces.IGraph;
import structures.classes.Edge;

public class GraphALTest {

    public GraphAL<Integer, Integer> graph;

    public void setupSimple1() {
        graph = new GraphAL<Integer, Integer>();
    }

    public void setupSimple2() {
        setupSimple1();

        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<Integer, Integer>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<Integer, Integer>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<Integer, Integer>(5, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));

    }

    public void setUp3() {
        graph = new GraphAL<Integer, Integer>();

        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 100);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 200);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 10));
        graph.insertEdge(new Edge<>(vertex2, vertex1, 20));
    }

    public void setUp4() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<>(5, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 8));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 2));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 9));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 5));
        graph.insertEdge(new Edge<>(vertex2, vertex5, 5));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 6));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 8));
    }

    public void setUp5() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<>(5, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 8));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 2));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 9));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 5));
        graph.insertEdge(new Edge<>(vertex2, vertex5, 5));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 6));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 8));
    }

    public void setUp6() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<>(5, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));
    }

    public void setUp7() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 2);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 3);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex3, 3));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 1));
    }

    public void setUp8() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<>(5, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 3));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 5));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 2));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));
    }

    public void setUp9() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<>(5, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 3));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));
    }

    public void setUp10() {
        graph = new GraphAL<>();

        Vertex<Integer, Integer> vertex1 = new Vertex<>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<>(2, 2);
        Vertex<Integer, Integer> vertex3 = new Vertex<>(3, 3);
        Vertex<Integer, Integer> vertex4 = new Vertex<>(4, 4);
        Vertex<Integer, Integer> vertex5 = new Vertex<>(5, 5);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 10));
        graph.insertEdge(new Edge<>(vertex2, vertex4, 5));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 2));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 11));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));
    }

    @Test
    public void test1InsertVertex() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(2, 2);
        graph.insertVertex(3, 3);
        graph.insertVertex(4, 4);

        assert (graph.getVertexAmount() == 4);
    }

    @Test
    public void test2InsertVertex() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(1, 1);
        graph.insertVertex(1, 1);
        graph.insertVertex(1, 1);

        assert (graph.getVertexAmount() == 1);
    }
    
    @Test
    public void test3InsertVertex() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(1, 2);
        graph.insertVertex(3, 3);
        graph.insertVertex(3, 4);

        assert (graph.getVertexAmount() == 2);
    }

    @Test
    public void test1InsertEdge() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(2, 2);
        graph.insertVertex(3, 3);
        graph.insertVertex(4, 4);

        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(2), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(3), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(4), 1));

        assert (graph.getEdgesAmount() == 3);
    }

    @Test
    public void test2InsertEdge() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(2, 2);
        graph.insertVertex(3, 3);
        graph.insertVertex(4, 4);

        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(2), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(3), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(4), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(4), 1));

        assert (graph.getEdgesAmount() == 3);
    }

    @Test
    public void test3InsertEdge() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(2, 2);
        graph.insertVertex(3, 3);
        graph.insertVertex(4, 4);

        graph.insertEdge(new Edge<>(graph.searchVertex(1), graph.searchVertex(2), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(2), graph.searchVertex(1), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(3), graph.searchVertex(4), 1));
        graph.insertEdge(new Edge<>(graph.searchVertex(4), graph.searchVertex(3), 2));

        assert (graph.getEdgesAmount() == 2);
    }

    @Test
    public void testInsertEdge() {
        setupSimple2();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex3 = graph.searchVertex(3);
        Vertex<Integer, Integer> vertex4 = graph.searchVertex(4);
        Vertex<Integer, Integer> vertex5 = graph.searchVertex(5);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);

        assertEquals(true, vertex2.isConnected(vertex1));
        assertEquals(true, vertex5.isConnected(vertex1));
        assertEquals(true, vertex4.isConnected(vertex1));
        assertEquals(true, vertex5.isConnected(vertex4));
        assertEquals(true, vertex3.isConnected(vertex4));
        assertEquals(true, vertex5.isConnected(vertex2));
        assertEquals(true, vertex3.isConnected(vertex2));
        assertEquals(true, vertex5.isConnected(vertex3));

    }

    @Test
    public void testIsConnected() {
        setupSimple2();

        graph.removeVertex(graph.searchVertex(1));

        assertNotNull(graph.searchVertex(2));
        assertEquals(false, graph.searchVertex(2).isConnected(graph.searchVertex(1)));

    }

    @Test
    public void testRemoveVertex() {
        setupSimple1();

        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<Integer, Integer>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<Integer, Integer>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<Integer, Integer>(5, 1);
        Vertex<Integer, Integer> vertex9 = new Vertex<Integer, Integer>(9, 9);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));

        graph.insertEdge(new Edge<>(vertex1, vertex9, 0));

        graph.removeVertex(vertex1);

        assertEquals(false, vertex2.isConnected(vertex1));
    }

    @Test
    public void testIsStronglyConex1() {
        setupSimple2();

        assertEquals(true, graph.isStronglyConex());
    }

    @Test
    public void testIsStronglyConex2() {
        setupSimple1();
        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<Integer, Integer>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<Integer, Integer>(4, 1);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex4, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex1, 1));

        assertEquals(true, graph.isStronglyConex());
    }

    @Test
    public void testInsertVertexAndSearchVertexSimpleGraph() {
        setupSimple1();
        graph.insertVertex(1, 100);
        graph.insertVertex(2, 200);
        graph.insertVertex(3, 300);
        graph.insertVertex(4, 400);

        assertNotNull(graph.searchVertex(1));
        assertNotNull(graph.searchVertex(2));
        assertNotNull(graph.searchVertex(3));
        assertNotNull(graph.searchVertex(4));
        assertNull(graph.searchVertex(5));
    }

    @Test
    public void test1Dijsktra(){
        setUp10();

        List<Edge<Integer, Integer>> path = graph.Dijkstra(graph.searchVertex(1), graph.searchVertex(5));

        assertEquals(4, path.size());
    }

    @Test
    public void test2Dijsktra(){
        setUp10();

        List<Edge<Integer, Integer>> path = graph.Dijkstra(graph.searchVertex(1), graph.searchVertex(5));

        assertEquals(4, path.get(1).getVertex2().getValue().intValue());
    }

    @Test
    public void test3Dijsktra(){
        setUp10();

        List<Edge<Integer, Integer>> path = graph.Dijkstra(graph.searchVertex(1), graph.searchVertex(5));

        assertEquals(5, path.get(3).getVertex2().getValue().intValue());
    }

    @Test
    public void test1FloydWarshall() {
        setUp5();

        int[][] expectedDistances = {
                { 0, 8, 13, 9, 2 },
                { 8, 0, 5, 11, 5 },
                { 13, 5, 0, 6, 11 },
                { 9, 11, 6, 0, 6 },
                { 2, 5, 11, 6, 0 }
        };

        int[][] distances = graph.floydWarshall();

        assertEquals(expectedDistances.length, distances.length);
        assertEquals(expectedDistances[0].length, distances[0].length);

    }

    @Test
    public void test2FloydWarshall() {

        setUp5();

        int[][] expectedDistances = {
                { 0, 8, 13, 9, 2 },
                { Integer.MAX_VALUE, 0, 5, 11, 5 },
                { Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 6, 11 },
                { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 6 },
                { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0 }
        };

        int[][] distances = graph.floydWarshall();

        assertEquals(expectedDistances[0].length, distances[0].length);
    }

    @Test
    public void test3FloydWarshall() {
        setUp5();

        int[][] expectedDistances = {
                { 0, 8, 13, 9, 2 },
                { Integer.MAX_VALUE, 0, 5, 11, 5 },
                { Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 6, 11 },
                { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 6 },
                { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0 }
        };

        int[][] distances = graph.floydWarshall();

        assertEquals(expectedDistances.length, distances.length);
    }

    @Test
    public void test1Prim() {
        setUp4();
        IGraph<Integer, Integer> mst = graph.prim(graph.getVertexList().get(0));

        assertEquals(5, mst.getVertexAmount());
        assertEquals(4, mst.getEdgesAmount());

        Vertex<Integer, Integer> mstVertex1 = mst.searchVertex(1);
        Vertex<Integer, Integer> mstVertex2 = mst.searchVertex(2);
        Vertex<Integer, Integer> mstVertex5 = mst.searchVertex(5);

        assertTrue(mstVertex1.isConnected(mstVertex2));
        assertTrue(mstVertex1.isConnected(mstVertex5));
    }

    @Test
    public void test2Prim() {
        setUp4();
        IGraph<Integer, Integer> mst = graph.prim(graph.getVertexList().get(0));

        assertEquals(5, mst.getVertexAmount());
        assertEquals(4, mst.getEdgesAmount());

        Vertex<Integer, Integer> mstVertex1 = mst.searchVertex(1);
        Vertex<Integer, Integer> mstVertex2 = mst.searchVertex(2);
        Vertex<Integer, Integer> mstVertex4 = mst.searchVertex(4);
        Vertex<Integer, Integer> mstVertex5 = mst.searchVertex(5);

        assertNotNull(mstVertex1);
        assertNotNull(mstVertex2);
        assertNotNull(mstVertex4);
        assertNotNull(mstVertex5);

        assertTrue(mstVertex1.isConnected(mstVertex2));
        assertTrue(mstVertex1.isConnected(mstVertex5));
    }

    @Test
    public void test3Prim() {
        setUp4();
        IGraph<Integer, Integer> mst = graph.prim(graph.getVertexList().get(0));

        assertEquals(5, mst.getVertexAmount());
        assertEquals(4, mst.getEdgesAmount());

        Vertex<Integer, Integer> mstVertex1 = mst.searchVertex(1);
        Vertex<Integer, Integer> mstVertex2 = mst.searchVertex(2);
        Vertex<Integer, Integer> mstVertex4 = mst.searchVertex(4);
        Vertex<Integer, Integer> mstVertex5 = mst.searchVertex(5);

        assertNotNull(mstVertex1);
        assertNotNull(mstVertex2);
        assertNotNull(mstVertex4);
        assertNotNull(mstVertex5);
    }

    @Test
    public void test1Kruskal() {
        setUp4();
        IGraph<Integer, Integer> mst = graph.kruskal();

        assertEquals(5, mst.getVertexAmount());

        Vertex<Integer, Integer> mstVertex1 = mst.searchVertex(1);
        Vertex<Integer, Integer> mstVertex2 = mst.searchVertex(2);
        Vertex<Integer, Integer> mstVertex5 = mst.searchVertex(5);

        assertTrue(mstVertex1.isConnected(mstVertex2));
        assertTrue(mstVertex1.isConnected(mstVertex5));
    }

    @Test
    public void test2Kruskal() {
        setUp4();
        IGraph<Integer, Integer> mst = graph.kruskal();

        assertEquals(5, mst.getVertexAmount());

        Vertex<Integer, Integer> mstVertex1 = mst.searchVertex(1);
        Vertex<Integer, Integer> mstVertex2 = mst.searchVertex(2);
        Vertex<Integer, Integer> mstVertex4 = mst.searchVertex(4);
        Vertex<Integer, Integer> mstVertex5 = mst.searchVertex(5);

        assertNotNull(mstVertex1);
        assertNotNull(mstVertex2);
        assertNotNull(mstVertex4);
        assertNotNull(mstVertex5);

        assertTrue(mstVertex1.isConnected(mstVertex2));
        assertTrue(mstVertex1.isConnected(mstVertex5));
    }

    @Test
    public void test3Kruskal() {
        setUp4();
        IGraph<Integer, Integer> mst = graph.kruskal();

        Vertex<Integer, Integer> mstVertex1 = mst.searchVertex(1);
        Vertex<Integer, Integer> mstVertex2 = mst.searchVertex(2);
        Vertex<Integer, Integer> mstVertex4 = mst.searchVertex(4);
        Vertex<Integer, Integer> mstVertex5 = mst.searchVertex(5);

        assertEquals(5, mst.getVertexAmount());
        assertNotNull(mstVertex1);
        assertNotNull(mstVertex2);
        assertNotNull(mstVertex4);
        assertNotNull(mstVertex5);
    }

    @Test
    public void test1BFS() {

        setUp6();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);
        Vertex<Integer, Integer> vertex3 = graph.searchVertex(3);
        Vertex<Integer, Integer> vertex4 = graph.searchVertex(4);
        Vertex<Integer, Integer> vertex5 = graph.searchVertex(5);

        graph.BFS(vertex1);

        assertEquals(Color.BLACK, vertex1.getColor());
        assertEquals(Color.BLACK, vertex2.getColor());
        assertEquals(Color.BLACK, vertex3.getColor());
        assertEquals(Color.BLACK, vertex4.getColor());
        assertEquals(Color.BLACK, vertex5.getColor());

        assertEquals(0, (int) vertex1.getDistance());
        assertEquals(1, (int) vertex2.getDistance());

        assertNull(vertex1.getPredecessor());
        assertEquals(vertex1, vertex2.getPredecessor());
        assertEquals(vertex2, vertex3.getPredecessor());
    }

    @Test
    public void test2BFS() {
        setUp6();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);
        Vertex<Integer, Integer> vertex3 = graph.searchVertex(3);
        Vertex<Integer, Integer> vertex4 = graph.searchVertex(4);
        Vertex<Integer, Integer> vertex5 = graph.searchVertex(5);

        graph.BFS(vertex3);

        assertEquals(Color.BLACK, vertex1.getColor());
        assertEquals(Color.BLACK, vertex2.getColor());

        assertEquals(2, (int) vertex1.getDistance());
        assertEquals(1, (int) vertex2.getDistance());

        assertNull(vertex3.getPredecessor());
        assertEquals(vertex3, vertex4.getPredecessor());
        assertEquals(vertex3, vertex5.getPredecessor());
    }

    @Test
    public void test3BFS() {

        setUp6();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);
        Vertex<Integer, Integer> vertex3 = graph.searchVertex(3);
        Vertex<Integer, Integer> vertex4 = graph.searchVertex(4);
        Vertex<Integer, Integer> vertex5 = graph.searchVertex(5);

        graph.BFS(vertex5);

        assertEquals(Color.BLACK, vertex1.getColor());
        assertEquals(Color.BLACK, vertex2.getColor());
        assertEquals(Color.BLACK, vertex3.getColor());
        assertEquals(Color.BLACK, vertex4.getColor());
        assertEquals(Color.BLACK, vertex5.getColor());

        assertEquals(1, (int) vertex4.getDistance());
        assertEquals(0, (int) vertex5.getDistance());

        assertNull(vertex5.getPredecessor());
        assertEquals(vertex5, vertex1.getPredecessor());
        assertEquals(vertex5, vertex2.getPredecessor());

    }

    @Test
    public void test1DFS() {

        setUp7();

        IGraph<Integer, Integer> mst = graph.prim(graph.getVertexList().get(0));

        int totalWeight = graph.DFS(mst);

        assertEquals(2, totalWeight);

    }

    @Test
    public void test2DFS() {
        
        setUp8();

        IGraph<Integer, Integer> mst = graph.prim(graph.getVertexList().get(0));

        int totalWeight = graph.DFS(mst);

        assertEquals(7, totalWeight);
    }

    @Test
    public void test3DFS() {

        setUp9();

        IGraph<Integer, Integer> mst = graph.prim(graph.getVertexList().get(0));

        int totalWeight = graph.DFS(mst);

        assertEquals(6, totalWeight);
    }

}