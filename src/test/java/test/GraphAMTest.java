package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import structures.classes.GraphAM;
import structures.classes.Vertex;
import structures.interfaces.IGraph;
import structures.classes.Edge;

public class GraphAMTest {

    public GraphAM<Integer, Integer> graph;

    public void setupSimple1() {
        graph = new GraphAM<Integer, Integer>();
    }

    public void setupSimple2() {
        setupSimple1();

        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 1);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 1);
        Vertex<Integer, Integer> vertex3 = new Vertex<Integer, Integer>(3, 1);
        Vertex<Integer, Integer> vertex4 = new Vertex<Integer, Integer>(4, 1);
        Vertex<Integer, Integer> vertex5 = new Vertex<Integer, Integer>(5, 1);

        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);
        graph.insertVertex(vertex3);
        graph.insertVertex(vertex4);
        graph.insertVertex(vertex5);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex1, vertex4, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex2, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex5, 1));
        graph.insertEdge(new Edge<>(vertex4, vertex3, 1));
        graph.insertEdge(new Edge<>(vertex3, vertex5, 1));

    }

    public void setup3() {
        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 100);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 200);

        graph.insertVertex(vertex1);
        graph.insertVertex(vertex2);

        graph.insertEdge(new Edge<>(vertex1, vertex2, 10));
        graph.insertEdge(new Edge<>(vertex1, vertex2, 20));
    }

    @Test
    public void testInsertVertex() {
        setupSimple1();
        graph.insertVertex(1, 1);
        graph.insertVertex(2, 2);
        graph.insertVertex(3, 3);
        graph.insertVertex(4, 4);

        assert (graph.getVertexAmount() == 4);
    }

    @Test
    public void testInsertSimpleEdge() {
        setupSimple2();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);
        Vertex<Integer, Integer> vertex3 = graph.searchVertex(3);
        Vertex<Integer, Integer> vertex4 = graph.searchVertex(4);
        Vertex<Integer, Integer> vertex5 = graph.searchVertex(5);

        assertEquals(true, graph.areConnected(vertex1, vertex2));
        assertEquals(true, graph.areConnected(vertex1, vertex5));
        assertEquals(true, graph.areConnected(vertex1, vertex4));
        assertEquals(true, graph.areConnected(vertex4, vertex5));
        assertEquals(true, graph.areConnected(vertex4, vertex3));
        assertEquals(true, graph.areConnected(vertex2, vertex5));
        assertEquals(true, graph.areConnected(vertex2, vertex3));
        assertEquals(true, graph.areConnected(vertex3, vertex5));

    }

    @Test
    public void testIsConnected() {
        setupSimple2();

        graph.removeVertex(graph.searchVertex(1));

        assertNotNull(graph.searchVertex(2));
        assertNull(graph.searchVertex(1));

        assertEquals(false, graph.areConnected(graph.searchVertex(2), graph.searchVertex(1)));

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
    public void test1Prim() {

        IGraph<Integer, Integer> graph = new GraphAM<>();
        
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

        IGraph<Integer, Integer> minimumSpanningTree = graph.prim(graph.getVertexList().get(0));

        assertEquals(4, minimumSpanningTree.getVertexAmount());
        assertEquals(3, minimumSpanningTree.getEdgesAmount());

        assertEquals(true, minimumSpanningTree.areConnected(minimumSpanningTree.searchVertex(1),
                minimumSpanningTree.searchVertex(5)));
        assertEquals(true, minimumSpanningTree.areConnected(minimumSpanningTree.searchVertex(1),
                minimumSpanningTree.searchVertex(2)));

    }

}
