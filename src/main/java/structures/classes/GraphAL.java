package structures.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import structures.enums.Color;
import structures.enums.GraphType;
import structures.interfaces.IGraph;

public class GraphAL<K, V> implements IGraph<K, V> {
    private ArrayList<Vertex<K, V>> vertexList;
    private ArrayList<Edge<K, V>> edgeList;

    public GraphType type;

    public GraphAL(GraphType type) {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.type = type;
    }

    @Override
    public int getVertexAmount() {
        return vertexList.size();
    }

    /**
     * @return the edgesAmount
     */
    @Override
    public int getEdgesAmount() {
        return edgeList.size();
    }

    /**
     * @return the vertexList
     */
    @Override
    public ArrayList<Vertex<K, V>> getVertexList() {
        return vertexList;
    }

    /**
     * @param vertexList the vertexList to set
     */
    @Override
    public void setVertexList(ArrayList<Vertex<K, V>> vertexList) {
        this.vertexList = vertexList;
    }

    /**
     * @return the edgeList
     */
    @Override
    public ArrayList<Edge<K, V>> getEdgeList() {
        return edgeList;
    }

    /**
     * @param edgeList the edgeList to set
     */
    @Override
    public void setEdgeList(ArrayList<Edge<K, V>> edgeList) {
        this.edgeList = edgeList;
    }

    /**
     * @return the type
     */
    @Override
    public GraphType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    @Override
    public void setType(GraphType type) {
        this.type = type;
    }

    @Override
    public Vertex<K, V> insertVertex(K key, V value) {

        if (searchVertex(key) != null) {
            return null;
        }

        Vertex<K, V> vertex = new Vertex<K, V>(key, value);

        this.vertexList.add(vertex);
        return vertex;
    }

    @Override
    public Edge<K, V> insertEdge(Edge<K, V> edge) {

        if (!vertexList.contains(edge.getVertex1())) {
            vertexList.add(edge.getVertex1());
        }

        if (!vertexList.contains(edge.getVertex2())) {
            vertexList.add(edge.getVertex2());
        }

        switch (this.type) {
            case Simple -> {
                return insertSimpleEdge(edge);
            }
            case Directed -> {
                return insertDirectedEdge(edge);
            }
            case Multigraph -> {
                return insertMultigraphEdge(edge);
            }
            case Pseudograph -> {
                return insertPseudographEdge(edge);
            }
            case DirectedMultigraph -> {
                return insertMultiDirectedEdge(edge);
            }

            default -> {
                return null;
            }
        }

    }

    private Edge<K, V> insertSimpleEdge(Edge<K, V> edge) {

        if (edge.getVertex2().equals(edge.getVertex1())) {
            return null;
        }

        if (edge.getVertex2().isConnected(edge.getVertex1())) {
            return null;
        }

        Vertex<K, V> vertex1 = edge.getVertex1();
        Vertex<K, V> vertex2 = edge.getVertex2();

        vertex1.getEdges().add(edge);
        vertex2.getEdges().add(new Edge<>(vertex2, vertex1, edge.getWeight()));

        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertDirectedEdge(Edge<K, V> edge) {

        if (edge.getVertex1().isConnected(edge.getVertex2())) {
            return null;
        }

        edge.getVertex1().addEdge(edge);
        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertMultigraphEdge(Edge<K, V> edge) {

        if (edge.getVertex2().equals(edge.getVertex1())) {
            return null;
        }

        edge.getVertex2().addEdge(new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight()));
        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertPseudographEdge(Edge<K, V> edge) {
        edge.getVertex1().addEdge(edge);
        edge.getVertex2().addEdge(new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight()));
        edgeList.add(edge);
        return edge;
    }

    private Edge<K, V> insertMultiDirectedEdge(Edge<K, V> edge) {
        edgeList.add(edge);
        return edge;
    }

    @Override
    public Vertex<K, V> removeVertex(Vertex<K, V> vertex) {
        if (vertex == null) {
            return null;
        }

        vertex.disconnect();

        this.vertexList.remove(vertex);

        return vertex;
    }

    @Override
    public Vertex<K, V> searchVertex(K key) {
        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getKey().equals(key)) {
                return vertex;
            }
        }

        return null;
    }

    @Override
    public void BFS(Vertex<K, V> vertex) {

        for (Vertex<K, V> u : vertexList) {
            u.setColor(Color.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setPredecessor(null);
        }

        vertex.setColor(Color.GRAY);
        vertex.setDistance(0);
        vertex.setPredecessor(null);

        Queue<Vertex<K, V>> queue = new LinkedList<>();
        queue.add(vertex);

        while (!queue.isEmpty()) {
            Vertex<K, V> u = queue.poll();
            for (Edge<K, V> edge : u.getEdges()) {
                Vertex<K, V> v = edge.getVertex2();
                if (v.getColor() == Color.WHITE) {
                    v.setColor(Color.GRAY);
                    v.setDistance(u.getDistance() + 1);
                    v.setPredecessor(u);
                    queue.add(v);
                }
            }
            u.setColor(Color.BLACK);
        }

    }

    public boolean isConex() {
        BFS(vertexList.get(0));

        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getColor() == Color.WHITE) {
                return false;
            }
        }

        return true;
    }

    public boolean isStronglyConex() {
        for (Vertex<K, V> vertex : vertexList) {
            BFS(vertex);

            if (vertex.getEdges().isEmpty()) {
                return false;
            }

            for (Edge<K, V> edge : vertex.getEdges()) {
                if (edge.getVertex2().getColor() == Color.WHITE) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void DFS() {
        for (Vertex<K, V> vertex : vertexList) {
            vertex.setColor(Color.WHITE);
            vertex.setPredecessor(null);
        }

        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getColor() == Color.WHITE) {
                DFSVisit(vertex);
            }
        }

    }

    private void DFSVisit(Vertex<K, V> u) {
        u.setColor(Color.GRAY);
        u.setTimeStampD(u.getTimeStampD() + 1);
        for (Edge<K, V> edge : u.getEdges()) {
            Vertex<K, V> v = edge.getVertex2();
            if (v.getColor() == Color.WHITE) {
                v.setPredecessor(u);
                DFSVisit(v);
            }
        }
        u.setColor(Color.BLACK);
        u.setTimeStampF(u.getTimeStampF() + 1);
    }

    @Override
    public List<Edge<K,V>> Dijkstra(Vertex<K, V> s) {
        s.setDistance(0);

        PriorityQueue<Vertex<K, V>> q = new PriorityQueue<>();

        for (Vertex<K, V> vertex : vertexList) {
            if (!vertex.equals(s)) {
                vertex.setDistance(Integer.MAX_VALUE);
            }
            vertex.setPredecessor(null);
            q.add(vertex);
        }

        while (!q.isEmpty()) {
            Vertex<K, V> u = q.poll();
            for (Edge<K, V> edge : u.getEdges()) {
                Vertex<K, V> v = edge.getVertex2();
                if (v.getDistance() > u.getDistance() + edge.getWeight()) {
                    v.setDistance(u.getDistance() + edge.getWeight());
                    v.setPredecessor(u);
                    q.remove(v);
                }
            }
        }

        List<Edge<K,V>> edgeList = new ArrayList<>();

        for (Vertex<K, V> vertex : vertexList) {
            if (!vertex.equals(s)) {
                edgeList.add(new Edge<>(vertex.getPredecessor(), vertex, vertex.getDistance()));
            }
        }

        return edgeList;

    }

    @Override
    public void FloydWarshall() {



    }

    @Override
    public IGraph<K, V> prim(Vertex<K, V> begin) {

        // Create a new graph to store the minimum spanning tree
        IGraph<K, V> graph = new GraphAL<>(GraphType.Simple);

        // Set all the vertices to white, infinite distance and null predecessor
        for (Vertex<K, V> vertex : vertexList) {
            // Set the color to white to indicate that the vertex has not been visited
            vertex.setColor(Color.WHITE);
            // Set the distance to infinite because we don't know the distance yet
            vertex.setDistance(Integer.MAX_VALUE);
            // Set the predecessor to null because we don't know the predecessor yet
            vertex.setPredecessor(null);
        }

        // Set the distance of the begin vertex to 0 because it is the first vertex 
        begin.setDistance(0);

        // Create a priority queue to store the vertices, the priority is the distance
        PriorityQueue<Vertex<K, V>> q = new PriorityQueue<>();

        // Add all the vertices to the priority queue
        for (Vertex<K, V> vertex : vertexList) {
            q.add(vertex);
        }

        // While the priority queue is not empty
        while (!q.isEmpty()) {

            // Get the vertex with the minimum distance
            Vertex<K, V> u = q.poll();

            // Iterate over the edges of the vertex
            for (Edge<K, V> edge : u.getEdges()) {
                // Get the vertex connected to the current vertex by the edge
                Vertex<K, V> v = edge.getVertex2();

                // if the priority queue contains the vertex and the distance of the vertex is greater than the edge weight
                // because if the distance is less than the edge weight, it means that the vertex has already been visited
                if (q.contains(v) && v.getDistance() > edge.getWeight()) {
                    // Update the distance of the vertex with the edge weight
                    v.setDistance(edge.getWeight());
                    // Update the predecessor of the vertex with the current vertex
                    v.setPredecessor(u);
                }
            }
        }

        // Build the minimum spanning tree
        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getPredecessor() != null) {
                graph.insertEdge(new Edge<>(vertex.getPredecessor(), vertex, vertex.getDistance()));
            }
        }

        return graph;
    }

    @Override
    public void Kruskal() {

    }

}
