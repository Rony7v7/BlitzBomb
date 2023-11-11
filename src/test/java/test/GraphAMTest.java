package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import structures.classes.GraphAM;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.classes.Edge;

public class GraphAMTest {

    public GraphAM<Integer, Integer> graph;

    public void setupSimple1() {
        graph = new GraphAM<Integer, Integer>(GraphType.Simple);
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

    public void setupDirected1() {
        graph = new GraphAM<>(GraphType.Directed);
    }

    public void setupDirected2() {
        setupDirected1();

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
    public void testInsertDirectedEdge() {
        setupDirected2();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex3 = graph.searchVertex(3);
        Vertex<Integer, Integer> vertex4 = graph.searchVertex(4);
        Vertex<Integer, Integer> vertex5 = graph.searchVertex(5);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);

        assertEquals(true, vertex1.isConnected(vertex2));
        assertEquals(true, vertex1.isConnected(vertex5));
        assertEquals(true, vertex1.isConnected(vertex4));
        assertEquals(true, vertex4.isConnected(vertex5));
        assertEquals(true, vertex4.isConnected(vertex3));
        assertEquals(true, vertex2.isConnected(vertex5));
        assertEquals(true, vertex2.isConnected(vertex3));
        assertEquals(true, vertex3.isConnected(vertex5));

        assertEquals(false, vertex2.isConnected(vertex1));
        assertEquals(false, vertex5.isConnected(vertex1));
        assertEquals(false, vertex4.isConnected(vertex1));
        assertEquals(false, vertex5.isConnected(vertex4));
        assertEquals(false, vertex3.isConnected(vertex4));
        assertEquals(false, vertex5.isConnected(vertex2));
        assertEquals(false, vertex3.isConnected(vertex2));
        assertEquals(false, vertex5.isConnected(vertex3));

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
        setupDirected1();

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
    public void testInsertEdgeAndIsConnectedDirectedGraph() {
        setupDirected1();

        Vertex<Integer, Integer> vertex1 = new Vertex<Integer, Integer>(1, 100);
        Vertex<Integer, Integer> vertex2 = new Vertex<Integer, Integer>(2, 200);
        Vertex<Integer, Integer> vertex3 = new Vertex<Integer, Integer>(3, 300);

        Edge<Integer, Integer> edge12 = new Edge<>(vertex1, vertex2, 10);
        Edge<Integer, Integer> edge23 = new Edge<>(vertex2, vertex3, 20);

        graph.insertEdge(edge12);
        graph.insertEdge(edge23);

        assertTrue(vertex1.isConnected(vertex2));
        assertTrue(vertex2.isConnected(vertex3));
        assertFalse(vertex3.isConnected(vertex1));
    }

    @Test
    public void testInsertMultigraphEdge() {
        graph = new GraphAM<>(GraphType.Multigraph);
        setup3();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);

        assertTrue(vertex1.isConnected(vertex2));
        assertTrue(vertex2.isConnected(vertex1));
    }

    @Test
    public void testInsertPseudographEdge() {
        graph = new GraphAM<>(GraphType.Pseudograph);
        setup3();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);

        assertTrue(vertex1.isConnected(vertex2));
        assertTrue(vertex2.isConnected(vertex1));
    }

    @Test
    public void testInsertMultiDirectedEdge() {
        graph = new GraphAM<>(GraphType.DirectedMultigraph);
        setup3();

        Vertex<Integer, Integer> vertex1 = graph.searchVertex(1);
        Vertex<Integer, Integer> vertex2 = graph.searchVertex(2);

        assertTrue(vertex1.isConnected(vertex2));
        assertFalse(vertex2.isConnected(vertex1));
    }

}
